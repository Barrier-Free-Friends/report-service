package org.bf.reportservice.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bf.global.domain.event.DomainEventBuilder;
import org.bf.global.domain.event.EventPublisher;
import org.bf.global.infrastructure.event.ReportCreatedEvent;
import org.bf.global.infrastructure.event.ReportDeletedEvent;
import org.bf.global.infrastructure.event.ReportMapImageInfo;
import org.bf.global.infrastructure.event.ReportMapInfoEvent;
import org.bf.global.infrastructure.exception.CustomException;
import org.bf.reportservice.domain.entity.ImageTag;
import org.bf.reportservice.domain.entity.Report;
import org.bf.reportservice.domain.entity.ReportImage;
import org.bf.reportservice.domain.exception.ReportErrorCode;
import org.bf.reportservice.domain.repository.ReportRepository;
import org.bf.reportservice.infrastructure.api.AiVerificationClient;
import org.bf.reportservice.infrastructure.api.dto.AiImageRequest;
import org.bf.reportservice.infrastructure.api.dto.AiVerificationResponse;
import org.bf.reportservice.presentation.dto.ReportCreateRequest;
import org.bf.reportservice.presentation.dto.ReportDeleteResponse;
import org.bf.reportservice.presentation.dto.ReportResponse;
import org.bf.reportservice.presentation.dto.ReportUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    private static final int POINT_REVOKE_PERIOD_DAYS = 7;

    private final ReportRepository reportRepository;
    private final AiVerificationClient aiVerificationClient;
    private final EventPublisher eventPublisher;
    private final DomainEventBuilder eventBuilder;

    /**
     * 제보 등록
     * 1) 요청 이미지 유효성 검증
     * 2) AI 서비스에 이미지 검증 요청
     * 3) 검증 성공 시 Report 및 ReportImage 엔티티 생성
     * 4) 검증 결과 반영 후 저장
     * 5) Report 생성에 따른 포인트 지급 / 지도 반영 이벤트 발행
     * 6) 응답 반환
     */
    @Transactional
    public ReportResponse createReport(ReportCreateRequest request, UUID userId) {

        if (request.images() == null || request.images().isEmpty() ) {
            throw new CustomException(ReportErrorCode.IMAGE_REQUIRED);
        }

        // AI 요청용 DTO 변환
        List<AiImageRequest> aiImages = request.images().stream()
                .map(i -> new AiImageRequest(
                        i.fileUrl(),
                        i.latitude(),
                        i.longitude(),
                        i.address()
                ))
                .toList();

        // AI 검증 요청
        AiVerificationResponse ai;
        try {
            ai = aiVerificationClient.requestVerification(aiImages);
        } catch (Exception e) {
            throw new CustomException(ReportErrorCode.AI_SERVICE_ERROR);
        }

        // AI 응답 유효성
        if (ai == null) {
            throw new CustomException(ReportErrorCode.AI_SERVICE_ERROR);
        }

        log.info("반환값: {}", ai);

        // 검증 실패 시 등록 불가
        if (!ai.isObstacle()) {
            throw new CustomException(ReportErrorCode.AI_VERIFICATION_FAILED);
        }

        // 검증 성공 시 등록
        ImageTag tag = ImageTag.from(ai.tag());

        Report report = Report.builder()
                .userId(userId)
                .title(request.title())
                .content(request.content())
                .tag(tag)
                .build();

        List<ReportImage> images = request.images().stream()
                .map(i -> ReportImage.create(
                        i.fileUrl(),
                        i.latitude(),
                        i.longitude(),
                        i.address()
                ))
                .toList();
        images.forEach(report::addImage);

        // 검증 결과 반영
        report.updateVerificationResult(true, ai.analysisResult());

        Report saved = reportRepository.save(report);

        // Report 생성에 따른 포인트 지급 이벤트 발행
        ReportCreatedEvent rawEvent = new ReportCreatedEvent(
                saved.getUserId(),
                tag.getPoint(),
                saved.getId(),
                "p_report"
        );

        ReportCreatedEvent pointEvent = eventBuilder.build(rawEvent);
        eventPublisher.publish(pointEvent);

        // todo: 포인트 획득 성공 시에만 반영되도록 수정
        report.markPointRewarded();

        // 맵 서비스용 이미지 정보 구성
        List<ReportMapImageInfo> mapImages = request.images().stream()
                .map(i -> new ReportMapImageInfo(
                        i.latitude(),
                        i.longitude(),
                        i.address()
                ))
                .toList();

        // Report 생성에 따른 지도 반영 이벤트 발행
        ReportMapInfoEvent mapRawEvent = new ReportMapInfoEvent(
                saved.getUserId(),
                tag.getCode(),
                mapImages,
                saved.getId(),
                "p_report"
        );

        ReportMapInfoEvent mapEvent = eventBuilder.build(mapRawEvent);
        eventPublisher.publish(mapEvent);

        return ReportResponse.from(saved);
    }

    /**
     * 제보글 수정
     * 1) 수정 대상 제보 존재 여부 확인
     * 2) 요청자가 작성자가 맞는지 확인
     * 3) 제목 및 내용 수정 (이미지 수정 불가)
     */
    @Transactional
    public ReportResponse updateReport(UUID reportId, UUID userId, ReportUpdateRequest request) {

        // 제보글 존재 여부 확인
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new CustomException(ReportErrorCode.REPORT_NOT_FOUND));

        // 작성자 본인 여부 확인
        if (!report.getUserId().equals(userId)) {
            throw new CustomException(ReportErrorCode.REPORT_FORBIDDEN);
        }

        // 제목 및 내용 수정
        report.updateContent(request.title(), request.content());

        return ReportResponse.from(report);
    }

    /**
     * 제보글 삭제
     */
    @Transactional
    public ReportDeleteResponse deleteReport(UUID reportId, UUID requesterUserId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new CustomException(ReportErrorCode.REPORT_NOT_FOUND));

        // 작성자 본인 여부 확인
        if (!report.getUserId().equals(requesterUserId)) {
            throw new CustomException(ReportErrorCode.REPORT_FORBIDDEN);
        }

        // 이미 삭제된 제보글인지 확인
        if (report.isDeleted()) {
            throw new CustomException(ReportErrorCode.REPORT_ALREADY_DELETED);
        }

        boolean pointRevokeRequested = false;

        // 제보글 작성 후 7일 이내에 삭제된 경우에만 포인트 회수 이벤트 발행
        if (report.isPointRewarded()) {
            LocalDateTime createdAt = report.getCreatedAt();
            LocalDateTime now = LocalDateTime.now();

            // 7일 경과 여부 확인
            boolean isWithinRevokePeriod = createdAt != null && createdAt.plusDays(POINT_REVOKE_PERIOD_DAYS).isAfter(now);

            if (isWithinRevokePeriod) {
                // 포인트 회수 처리를 위한 제보 삭제 도메인 이벤트 발행
                ReportDeletedEvent rawEvent = new ReportDeletedEvent(
                        report.getUserId(),
                        report.getId(),
                        "p_report"
                );

                ReportDeletedEvent event = eventBuilder.build(rawEvent);
                eventPublisher.publish(event);

                pointRevokeRequested = true;
            }
        }

        // 소프트 삭제
        report.delete(requesterUserId.toString());

        return new ReportDeleteResponse(
                report.getId(),
                pointRevokeRequested,
                report.getDeletedAt()
        );
    }
}

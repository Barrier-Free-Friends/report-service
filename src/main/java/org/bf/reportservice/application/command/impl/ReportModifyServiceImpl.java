package org.bf.reportservice.application.command.impl;

import lombok.RequiredArgsConstructor;
import org.bf.global.infrastructure.exception.CustomException;
import org.bf.reportservice.application.command.ReportModifyService;
import org.bf.reportservice.application.event.ReportDomainEventPublisher;
import org.bf.reportservice.domain.entity.Report;
import org.bf.reportservice.domain.exception.ReportErrorCode;
import org.bf.reportservice.domain.repository.ReportRepository;
import org.bf.reportservice.presentation.dto.ReportDeleteResponse;
import org.bf.reportservice.presentation.dto.ReportResponse;
import org.bf.reportservice.presentation.dto.ReportUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportModifyServiceImpl implements ReportModifyService {

    private static final int POINT_REVOKE_PERIOD_DAYS = 7;

    private final ReportRepository reportRepository;
    private final ReportDomainEventPublisher reportDomainEventPublisher;

    /**
     * 제보글 수정
     * 1) 수정 대상 제보 존재 여부 확인
     * 2) 요청자가 작성자가 맞는지 확인
     * 3) 제목 및 내용 수정 (이미지 수정 불가)
     */
    @Transactional
    @Override
    public ReportResponse update(UUID reportId, UUID userId, ReportUpdateRequest request) {

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
    @Override
    public ReportDeleteResponse delete(UUID reportId, UUID requesterUserId) {

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
                reportDomainEventPublisher.publishDeleted(report);
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

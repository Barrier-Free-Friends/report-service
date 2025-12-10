package org.bf.reportservice.application;

import lombok.RequiredArgsConstructor;
import org.bf.global.infrastructure.exception.CustomException;
import org.bf.reportservice.domain.entity.Report;
import org.bf.reportservice.domain.entity.ReportImage;
import org.bf.reportservice.domain.exception.ReportErrorCode;
import org.bf.reportservice.domain.repository.ReportRepository;
import org.bf.reportservice.infrastructure.api.AiVerificationClient;
import org.bf.reportservice.infrastructure.api.dto.AiImageRequest;
import org.bf.reportservice.infrastructure.api.dto.AiVerificationResponse;
import org.bf.reportservice.presentation.dto.ReportRequest;
import org.bf.reportservice.presentation.dto.ReportResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final AiVerificationClient aiVerificationClient;

    /**
     * 제보 등록
     * 1) 요청 이미지 유효성 검증
     * 2) AI 서비스에 이미지 검증 요청
     * 3) 검증 성공 시 Report 및 ReportImage 엔티티 생성
     * 4) 검증 결과 반영 상태 저장 후 응답 반환
     */
    @Transactional
    public ReportResponse createReport(ReportRequest request, UUID userId) {

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

        // 검증 실패 시 등록 불가
        if (ai == null || !ai.verified()) {
            throw new CustomException(ReportErrorCode.AI_VERIFICATION_FAILED);
        }

        Report report = Report.builder()
                .userId(userId)
                .title(request.title())
                .content(request.content())
                .category(request.category())
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
        report.updateVerificationResult(true, ai.message());

        Report saved = reportRepository.save(report);
        return ReportResponse.from(saved);
    }
}

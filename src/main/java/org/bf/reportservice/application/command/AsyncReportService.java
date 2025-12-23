package org.bf.reportservice.application.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bf.global.infrastructure.exception.CustomException;
import org.bf.reportservice.application.event.AiVerificationEventPublisher;
import org.bf.reportservice.domain.entity.Report;
import org.bf.reportservice.domain.exception.ReportErrorCode;
import org.bf.reportservice.domain.repository.ReportRepository;
import org.bf.reportservice.presentation.dto.ReportResponse;
import org.bf.reportservice.presentation.dto.async.AsyncReportCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsyncReportService {

    private final ReportRepository reportRepository;
    private final AiVerificationEventPublisher aiVerificationEventPublisher;

    /**
     * 비동기 제보 등록
     * - 제보 기본 정보만 먼저 저장 (PENDING)
     * - AI 검증 요청 이벤트 발행
     * - 검증 결과 반영은 별도 리스너에서 처리
     */
    @Transactional
    public ReportResponse createReport(AsyncReportCreateRequest request, UUID userId) {

        if (request == null || request.images() == null || request.images().isEmpty()) {
            throw new CustomException(ReportErrorCode.IMAGE_REQUIRED);
        }

        // 검증 전 -> tag는 null 상태로 저장
        Report report = Report.builder()
                .userId(userId)
                .title(request.title())
                .content(request.content())
                .tag(null)
                .build();

        reportRepository.save(report);

        // AI 검증 요청 이벤트 발행
        aiVerificationEventPublisher.publishRequested(report.getId(), request);

        return ReportResponse.from(report);
    }

    /**
     * 비동기 등록된 제보 조회
     */
    @Transactional(readOnly = true)
    public ReportResponse getReport(UUID reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new CustomException(ReportErrorCode.REPORT_NOT_FOUND));
        return ReportResponse.from(report);
    }
}

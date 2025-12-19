package org.bf.reportservice.application.query.impl;

import lombok.RequiredArgsConstructor;
import org.bf.global.infrastructure.exception.CustomException;
import org.bf.reportservice.application.query.ReportQueryService;
import org.bf.reportservice.domain.exception.ReportErrorCode;
import org.bf.reportservice.infrastructure.persistence.ReportQueryRepository;
import org.bf.reportservice.presentation.dto.ReportDetailResponse;
import org.bf.reportservice.presentation.dto.ReportSearchRequest;
import org.bf.reportservice.presentation.dto.ReportSearchResponse;
import org.bf.reportservice.presentation.dto.ReportSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportQueryServiceImpl implements ReportQueryService {

    private final ReportQueryRepository reportQueryRepository;

    /**
     * 제보글 목록 조회
     */
    @Override
    public Page<ReportSummaryResponse> getReports(Pageable pageable) {
        return reportQueryRepository.findReports(pageable);
    }

    /**
     * 제보글 상세 조회
     */
    @Override
    public ReportDetailResponse getReport(UUID reportId) {
        return reportQueryRepository.findReportDetail(reportId)
                .orElseThrow(() -> new CustomException(ReportErrorCode.REPORT_NOT_FOUND));
    }

    /**
     * 제보글 검색
     */
    @Override
    public Page<ReportSearchResponse> searchReports(ReportSearchRequest request, Pageable pageable) {
        return reportQueryRepository.searchReports(request, pageable);
    }
}

package org.bf.reportservice.application.query.impl;

import lombok.RequiredArgsConstructor;
import org.bf.global.infrastructure.exception.CustomException;
import org.bf.reportservice.application.query.ReportQueryService;
import org.bf.reportservice.domain.exception.ReportErrorCode;
import org.bf.reportservice.infrastructure.persistence.ReportQueryRepository;
import org.bf.reportservice.presentation.dto.ReportDetailResponse;
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

    @Override
    public Page<ReportSummaryResponse> getReports(Pageable pageable) {
        return reportQueryRepository.findReports(pageable);
    }

    @Override
    public ReportDetailResponse getReport(UUID reportId) {
        return reportQueryRepository.findReportDetail(reportId)
                .orElseThrow(() -> new CustomException(ReportErrorCode.REPORT_NOT_FOUND));
    }
}

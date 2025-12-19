package org.bf.reportservice.application.query;

import org.bf.reportservice.presentation.dto.ReportDetailResponse;
import org.bf.reportservice.presentation.dto.ReportSearchRequest;
import org.bf.reportservice.presentation.dto.ReportSearchResponse;
import org.bf.reportservice.presentation.dto.ReportSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ReportQueryService {
    Page<ReportSummaryResponse> getReports(Pageable pageable);
    ReportDetailResponse getReport(UUID reportId);
    Page<ReportSearchResponse> searchReports(ReportSearchRequest request, Pageable pageable);
}

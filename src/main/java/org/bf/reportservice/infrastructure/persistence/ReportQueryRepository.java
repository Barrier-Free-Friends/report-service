package org.bf.reportservice.infrastructure.persistence;

import org.bf.reportservice.presentation.dto.ReportDetailResponse;
import org.bf.reportservice.presentation.dto.ReportSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ReportQueryRepository {
    Page<ReportSummaryResponse> findReports(Pageable pageable);
    Optional<ReportDetailResponse> findReportDetail(UUID reportId);
}

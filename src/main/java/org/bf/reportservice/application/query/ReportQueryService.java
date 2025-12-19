package org.bf.reportservice.application.query;

import org.bf.reportservice.presentation.dto.ReportDetailResponse;
import org.bf.reportservice.presentation.dto.ReportSearchRequest;
import org.bf.reportservice.presentation.dto.ReportSearchResponse;
import org.bf.reportservice.presentation.dto.ReportSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ReportQueryService {
    /**
     * 제보글 목록 조회
     */
    Page<ReportSummaryResponse> getReports(Pageable pageable);

    /**
     * 제보글 상세 조회
     */
    ReportDetailResponse getReport(UUID reportId);

    /**
     * 제보글 검색
     */
    Page<ReportSearchResponse> searchReports(ReportSearchRequest request, Pageable pageable);
}

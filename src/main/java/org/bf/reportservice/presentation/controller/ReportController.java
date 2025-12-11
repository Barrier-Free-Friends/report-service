package org.bf.reportservice.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.bf.global.infrastructure.CustomResponse;
import org.bf.global.infrastructure.success.GeneralSuccessCode;
import org.bf.global.security.SecurityUtils;
import org.bf.reportservice.application.ReportService;
import org.bf.reportservice.presentation.dto.ReportCreateRequest;
import org.bf.reportservice.presentation.dto.ReportDeleteResponse;
import org.bf.reportservice.presentation.dto.ReportResponse;
import org.bf.reportservice.presentation.dto.ReportUpdateRequest;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final SecurityUtils securityUtils;

    /**
     * 제보글 등록
     */
    @PostMapping
    public CustomResponse<ReportResponse> createReport(@RequestBody ReportCreateRequest request) {
        ReportResponse response = reportService.createReport(request, securityUtils.getCurrentUserId());
        return CustomResponse.onSuccess(GeneralSuccessCode.CREATED, response);
    }

    /**
     * 제보글 수정
     */
    @PatchMapping("/{reportId}")
    public CustomResponse<ReportResponse> updateReport(@PathVariable UUID reportId, @RequestBody ReportUpdateRequest request) {
        ReportResponse response = reportService.updateReport(reportId, securityUtils.getCurrentUserId(), request);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, response);
    }

    /**
     * 제보글 삭제
     */
    @DeleteMapping("/{reportId}")
    public CustomResponse<ReportDeleteResponse> deleteReport(@PathVariable UUID reportId) {
        ReportDeleteResponse response = reportService.deleteReport(reportId, securityUtils.getCurrentUserId());
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, response);
    }
}

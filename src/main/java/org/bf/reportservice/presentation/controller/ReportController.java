package org.bf.reportservice.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bf.global.infrastructure.CustomResponse;
import org.bf.global.infrastructure.success.GeneralSuccessCode;
import org.bf.global.security.SecurityUtils;
import org.bf.reportservice.application.ReportService;
import org.bf.reportservice.application.query.ReportQueryService;
import org.bf.reportservice.presentation.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Report API", description = "제보글 등록/조회/수정/삭제 및 검색이 가능한 API")
public class ReportController {

    private final ReportService reportService;
    private final SecurityUtils securityUtils;
    private final ReportQueryService reportQueryService;

    @Operation(summary = "제보글 등록")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CustomResponse<ReportResponse> createReport(@Valid @RequestPart("request") ReportCreateRequest request,
                                                       @RequestPart("files") List<MultipartFile> files) {
        ReportResponse response = reportService.createReport(request, files, securityUtils.getCurrentUserId());
        return CustomResponse.onSuccess(GeneralSuccessCode.CREATED, response);
    }

    @Operation(summary = "제보글 목록 조회")
    @GetMapping
    public CustomResponse<Page<ReportSummaryResponse>> getReports(Pageable pageable) {
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, reportQueryService.getReports(pageable));
    }

    @Operation(summary = "제보글 상세 조회")
    @GetMapping("/detail/{reportId}")
    public CustomResponse<ReportDetailResponse> getReport(@PathVariable UUID reportId) {
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, reportQueryService.getReport(reportId));
    }

    @Operation(summary = "제보글 수정")
    @PatchMapping("/{reportId}")
    public CustomResponse<ReportResponse> updateReport(@PathVariable UUID reportId, @Valid @RequestBody ReportUpdateRequest request) {
        ReportResponse response = reportService.updateReport(reportId, securityUtils.getCurrentUserId(), request);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, response);
    }

    @Operation(summary = "제보글 삭제")
    @DeleteMapping("/{reportId}")
    public CustomResponse<ReportDeleteResponse> deleteReport(@PathVariable UUID reportId) {
        ReportDeleteResponse response = reportService.deleteReport(reportId, securityUtils.getCurrentUserId());
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, response);
    }

    @Operation(summary = "제보글 검색")
    @GetMapping("/search")
    public CustomResponse<Page<ReportSearchResponse>> searchReports(@ModelAttribute ReportSearchRequest request, Pageable pageable) {
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, reportQueryService.searchReports(request, pageable));
    }
}

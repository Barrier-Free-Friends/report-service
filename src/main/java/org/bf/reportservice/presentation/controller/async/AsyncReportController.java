package org.bf.reportservice.presentation.controller.async;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bf.global.infrastructure.CustomResponse;
import org.bf.global.infrastructure.success.GeneralSuccessCode;
import org.bf.global.security.SecurityUtils;
import org.bf.reportservice.application.command.AsyncReportService;
import org.bf.reportservice.presentation.dto.ReportResponse;
import org.bf.reportservice.presentation.dto.async.AsyncReportCreateRequest;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/async")
@Tag(name = "Async Report API", description = "비동기 제보글 등록/조회 API")
public class AsyncReportController {

    private final AsyncReportService asyncReportService;
    private final SecurityUtils securityUtils;

    @Operation(summary = "비동기 제보글 등록")
    @PostMapping
    public CustomResponse<ReportResponse> createReport(@Valid @RequestBody AsyncReportCreateRequest request) {
        ReportResponse response = asyncReportService.createReport(request, securityUtils.getCurrentUserId());
        return CustomResponse.onSuccess(GeneralSuccessCode.CREATED, response);
    }

    @Operation(summary = "비동기 제보글 조회")
    @GetMapping("/{reportId}")
    public CustomResponse<ReportResponse> getReport(@PathVariable UUID reportId) {
        ReportResponse response = asyncReportService.getReport(reportId);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, response);
    }
}

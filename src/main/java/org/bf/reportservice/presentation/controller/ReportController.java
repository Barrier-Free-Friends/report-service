package org.bf.reportservice.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.bf.global.infrastructure.CustomResponse;
import org.bf.global.infrastructure.success.GeneralSuccessCode;
import org.bf.global.security.SecurityUtils;
import org.bf.reportservice.application.ReportService;
import org.bf.reportservice.presentation.dto.ReportCreateRequest;
import org.bf.reportservice.presentation.dto.ReportResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final SecurityUtils securityUtils;

    @PostMapping
    public CustomResponse<ReportResponse> createReport(@RequestBody ReportCreateRequest request) {

        ReportResponse response = reportService.createReport(request, securityUtils.getCurrentUserId());
        return CustomResponse.onSuccess(GeneralSuccessCode.CREATED, response);
    }
}

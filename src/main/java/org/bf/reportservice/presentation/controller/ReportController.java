package org.bf.reportservice.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.bf.global.infrastructure.CustomResponse;
import org.bf.global.infrastructure.exception.CustomException;
import org.bf.global.infrastructure.success.GeneralSuccessCode;
import org.bf.reportservice.application.ReportService;
import org.bf.reportservice.domain.exception.ReportErrorCode;
import org.bf.reportservice.presentation.dto.ReportRequest;
import org.bf.reportservice.presentation.dto.ReportResponse;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public CustomResponse<ReportResponse> createReport(@RequestHeader("X-USER-ID") String userIdHeader,
                                                        @RequestBody ReportRequest request) {
        UUID userId;
        try {
            userId = UUID.fromString(userIdHeader);
        } catch (IllegalArgumentException e) {
            throw new CustomException(ReportErrorCode.INVALID_USER_ID);
        }

        ReportResponse response = reportService.createReport(request, userId);
        return CustomResponse.onSuccess(GeneralSuccessCode.CREATED, response);
    }
}

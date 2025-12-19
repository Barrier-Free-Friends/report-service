package org.bf.reportservice.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.bf.reportservice.domain.entity.Report;

import java.util.UUID;

@Schema(description = "제보글 처리 결과 응답 DTO")
public record ReportResponse(

        @Schema(description = "제보글 ID")
        UUID reportId,

        @Schema(description = "제보글 상태")
        String reportStatus,

        @Schema(description = "AI 검증 상태")
        String verificationStatus,

        @Schema(description = "AI 검증 메시지")
        String verificationMessage
) {
    public static ReportResponse from(Report report) {
        return new ReportResponse(
                report.getId(),
                report.getReportStatus().name(),
                report.getVerificationStatus().name(),
                report.getVerificationMessage()
        );
    }
}

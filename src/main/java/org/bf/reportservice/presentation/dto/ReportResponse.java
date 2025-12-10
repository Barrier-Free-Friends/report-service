package org.bf.reportservice.presentation.dto;

import org.bf.reportservice.domain.entity.Report;

import java.util.UUID;

public record ReportResponse(
        UUID reportId,
        String reportStatus,
        String verificationStatus,
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

package org.bf.reportservice.presentation.dto;

import org.bf.reportservice.domain.entity.ReportStatus;
import org.bf.reportservice.domain.entity.VerificationStatus;

import java.util.UUID;

public record ReportResponse(
        UUID reportId,
        ReportStatus reportStatus,
        VerificationStatus verificationStatus
) {}

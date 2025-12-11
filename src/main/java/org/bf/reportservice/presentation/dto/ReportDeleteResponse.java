package org.bf.reportservice.presentation.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReportDeleteResponse(
        UUID reportId,
        boolean pointRevokeRequested,
        LocalDateTime deletedAt
) {}

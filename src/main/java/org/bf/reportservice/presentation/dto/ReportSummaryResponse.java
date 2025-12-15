package org.bf.reportservice.presentation.dto;

import org.bf.reportservice.domain.entity.ImageTag;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReportSummaryResponse(
        UUID reportId,
        String title,
        ImageTag tag,
        LocalDateTime createdAt
) {}

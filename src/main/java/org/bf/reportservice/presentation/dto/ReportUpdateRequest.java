package org.bf.reportservice.presentation.dto;

import org.bf.reportservice.domain.entity.ReportCategory;

public record ReportUpdateRequest(
        String title,
        String content,
        ReportCategory category
) {}

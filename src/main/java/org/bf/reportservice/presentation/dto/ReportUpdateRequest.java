package org.bf.reportservice.presentation.dto;

public record ReportUpdateRequest(
        String title,
        String content
) {}

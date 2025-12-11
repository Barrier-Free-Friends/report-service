package org.bf.reportservice.presentation.dto;

import org.bf.reportservice.domain.entity.ReportCategory;

import java.util.List;

public record ReportCreateRequest(
        String title,
        String content,
        ReportCategory category,
        List<ReportImageRequest> images
) {}

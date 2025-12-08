package org.bf.reportservice.presentation.dto;

import java.util.List;

public record ReportRequest(
        String title,
        String content,
        String category,
        List<ReportImageRequest> images
) {}

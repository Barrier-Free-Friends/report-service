package org.bf.reportservice.presentation.dto;

import java.util.List;

public record ReportCreateRequest(
        String title,
        String content,
        List<ImageMeta> images
) {
    public record ImageMeta(
            Double latitude,
            Double longitude,
            String address
    ) {}
}

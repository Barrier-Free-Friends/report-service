package org.bf.reportservice.presentation.dto;

import org.bf.reportservice.domain.entity.ImageTag;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ReportDetailResponse(
        UUID reportId,
        UUID userId,
        String title,
        String content,
        ImageTag tag,
        LocalDateTime createdAt,
        List<ReportImageDto> images
) {
    public record ReportImageDto(
            String fileUrl,
            Double latitude,
            Double longitude,
            String address
    ) {}
}

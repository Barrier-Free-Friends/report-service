package org.bf.reportservice.presentation.dto.async;

import java.util.List;

/**
 * 비동기 제보 등록 요청 DTO
 */
public record AsyncReportCreateRequest(
        String title,
        String content,
        List<ImageMeta> images
) {
    public record ImageMeta(
            String fileUrl,
            Double latitude,
            Double longitude,
            String address
    ) {}
}

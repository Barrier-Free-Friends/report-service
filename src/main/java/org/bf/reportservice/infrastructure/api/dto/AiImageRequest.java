package org.bf.reportservice.infrastructure.api.dto;

/**
 * AI로 검증할 이미지 정보
 */
public record AiImageRequest(
        String fileUrl,
        Double latitude,
        Double longitude,
        String address
) {}

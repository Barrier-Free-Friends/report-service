package org.bf.reportservice.infrastructure.event;

/**
 * AI 이미지 검증 요청에 사용되는 이미지 메타 정보
 */
public record AiImageMeta(
        String fileUrl,
        Double latitude,
        Double longitude,
        String address
) {}

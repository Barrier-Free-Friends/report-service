package org.bf.reportservice.infrastructure.api.dto;

import java.util.List;

/**
 * AI 검증 요청 DTO
 */
public record AiVerificationRequest(
        List<AiImageRequest> images
) {}

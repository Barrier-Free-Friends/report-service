package org.bf.reportservice.infrastructure.api.dto;

public record AiVerificationResponse(
        boolean verified,
        String message
) {}

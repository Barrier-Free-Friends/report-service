package org.bf.reportservice.infrastructure.api.dto;

public record AiImageRequest(
        String fileUrl,
        Double latitude,
        Double longitude,
        String address
) {}

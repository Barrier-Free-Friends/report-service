package org.bf.reportservice.infrastructure.api.dto;

import java.util.List;

public record AiVerificationRequest(
        List<AiImageRequest> images
) {}

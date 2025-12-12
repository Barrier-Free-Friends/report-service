package org.bf.reportservice.infrastructure.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AiVerificationResponse(
        @JsonProperty("analysis_result")
        String analysisResult,

        @JsonProperty("is_obstacle")
        boolean isObstacle,

        String tag
) {}

package org.bf.reportservice.infrastructure.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * AI 이미지 검증 응답 DTO
 */
public record AiVerificationResponse(

        /**
         * 분석 결과
         */
        @JsonProperty("analysis_result")
        String analysisResult,

        /**
         * 장애물 여부
         */
        @JsonProperty("is_obstacle")
        boolean isObstacle,

        /**
         * 감지된 태그 코드
         */
        String tag
) {}

package org.bf.reportservice.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.bf.reportservice.domain.entity.ImageTag;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "제보글 검색 결과 응답 DTO")
public record ReportSearchResponse(

        @Schema(description = "제보글 ID")
        UUID reportId,

        @Schema(description = "제보 제목")
        String title,

        @Schema(description = "장애물 태그", example = "CONSTRUCTION")
        ImageTag tag,

        @Schema(description = "제보 등록 시각")
        LocalDateTime createdAt
) {}

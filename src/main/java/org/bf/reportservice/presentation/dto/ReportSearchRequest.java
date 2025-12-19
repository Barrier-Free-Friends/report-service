package org.bf.reportservice.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.bf.reportservice.domain.entity.ImageTag;

import java.time.LocalDateTime;

@Schema(description = "제보글 검색 요청 DTO")
public record ReportSearchRequest(

        @Schema(description = "검색 키워드 (제목/내용)", example = "공사")
        String keyword,

        @Schema(description = "장애물 태그", example = "CONSTRUCTION")
        ImageTag tag,

        @Schema(description = "검색 시작 날짜", example = "2025-01-01T00:00:00")
        LocalDateTime from,

        @Schema(description = "검색 종료 날짜", example = "2025-12-31T23:59:59")
        LocalDateTime to
) {}

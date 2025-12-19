package org.bf.reportservice.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "제보글 정보 수정 요청 DTO")
public record ReportUpdateRequest(

        @Schema(description = "수정할 제목")
        String title,

        @Schema(description = "수정할 내용")
        String content
) {}

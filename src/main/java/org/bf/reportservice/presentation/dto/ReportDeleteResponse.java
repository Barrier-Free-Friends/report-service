package org.bf.reportservice.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "제보글 삭제 응답 DTO")
public record ReportDeleteResponse(

        @Schema(description = "삭제된 제보글 ID")
        UUID reportId,

        @Schema(description = "포인트 회수 이벤트 요청 여부")
        boolean pointRevokeRequested,

        @Schema(description = "삭제 처리 시각")
        LocalDateTime deletedAt
) {}

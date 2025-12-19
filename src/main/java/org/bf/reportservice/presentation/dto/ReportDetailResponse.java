package org.bf.reportservice.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.bf.reportservice.domain.entity.ImageTag;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema(description = "제보글 상세 조회 응답 DTO")
public record ReportDetailResponse(

        @Schema(description = "제보글 ID")
        UUID reportId,

        @Schema(description = "작성자 사용자 ID")
        UUID userId,

        @Schema(description = "제보 제목")
        String title,

        @Schema(description = "제보 내용")
        String content,

        @Schema(description = "장애물 태그", example = "CONSTRUCTION")
        ImageTag tag,

        @Schema(description = "제보 등록 시각")
        LocalDateTime createdAt,

        @Schema(description = "제보 이미지 목록")
        List<ReportImageDto> images
) {
    @Schema(description = "제보 이미지 정보")
    public record ReportImageDto(
            @Schema(description = "이미지 접근 URL")
            String fileUrl,

            @Schema(description = "위도")
            Double latitude,

            @Schema(description = "경도")
            Double longitude,

            @Schema(description = "주소")
            String address
    ) {}
}

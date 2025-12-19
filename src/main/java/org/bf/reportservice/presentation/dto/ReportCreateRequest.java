package org.bf.reportservice.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "제보글 등록 요청 DTO")
public record ReportCreateRequest(

        @Schema(description = "제보 제목", example = "공사 현장 목격")
        String title,

        @Schema(description = "제보 내용", example = "공사가 진행 중이라서 올립니다.")
        String content,

        @Schema(description = "이미지 메타데이터 목록")
        List<ImageMeta> images
) {
    public record ImageMeta(

            @Schema(description = "위도", example = "37.490371")
            Double latitude,

            @Schema(description = "경도", example = "127.034239")
            Double longitude,

            @Schema(description = "주소", example = "서울특별시 강남구 강남대로 358 (역삼동)")
            String address
    ) {}
}

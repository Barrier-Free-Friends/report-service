package org.bf.reportservice.infrastructure.api;

import org.bf.reportservice.infrastructure.api.dto.AiImageRequest;
import org.bf.reportservice.infrastructure.api.dto.AiVerificationRequest;
import org.bf.reportservice.infrastructure.api.dto.AiVerificationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * AI 이미지 검증 서비스 호출 클라이언트
 */
@FeignClient(name = "image-ai-service")
public interface AiVerificationClient {

    /**
     * AI 이미지 검증 요청
     */
    @PostMapping("/analyze")
    AiVerificationResponse verify(@RequestBody AiVerificationRequest body);

    /**
     * 이미지 정보 기반 AI 검증 요청
     */
    default AiVerificationResponse requestVerification(List<AiImageRequest> images) {
        return verify(new AiVerificationRequest(images));
    }
}

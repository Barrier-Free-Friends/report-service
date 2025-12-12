package org.bf.reportservice.infrastructure.api;

import org.bf.reportservice.infrastructure.api.dto.AiImageRequest;
import org.bf.reportservice.infrastructure.api.dto.AiVerificationRequest;
import org.bf.reportservice.infrastructure.api.dto.AiVerificationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "image-ai-service")
public interface AiVerificationClient {

    @PostMapping("/analyze")
    AiVerificationResponse verify(@RequestBody AiVerificationRequest body);

    default AiVerificationResponse requestVerification(List<AiImageRequest> images) {
        return verify(new AiVerificationRequest(images));
    }
}

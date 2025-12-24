package org.bf.reportservice.application.event;

import org.bf.reportservice.presentation.dto.async.AsyncReportCreateRequest;

import java.util.UUID;

public interface AiVerificationEventPublisher {
    /**
     * AI 이미지 검증 요청 이벤트 발행
     */
    void publishRequested(UUID reportId, AsyncReportCreateRequest request);
}

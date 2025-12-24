package org.bf.reportservice.application.event.impl;

import lombok.RequiredArgsConstructor;
import org.bf.global.domain.event.DomainEventBuilder;
import org.bf.global.domain.event.EventPublisher;
import org.bf.reportservice.application.event.AiVerificationEventPublisher;
import org.bf.reportservice.infrastructure.event.AiImageMeta;
import org.bf.reportservice.infrastructure.event.AiVerificationRequestedEvent;
import org.bf.reportservice.presentation.dto.async.AsyncReportCreateRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AiVerificationEventPublisherImpl implements AiVerificationEventPublisher {

    private final EventPublisher eventPublisher;
    private final DomainEventBuilder eventBuilder;

    /**
     * AI 이미지 검증 요청 이벤트 발행
     */
    @Override
    public void publishRequested(UUID reportId, AsyncReportCreateRequest request) {

        List<AiImageMeta> images = request.images().stream()
                .map(i -> new AiImageMeta(i.fileUrl(), i.latitude(), i.longitude(), i.address()))
                .toList();

        // AI 검증 요청 이벤트 생성
        AiVerificationRequestedEvent rawEvent = new AiVerificationRequestedEvent(
                images,
                reportId,
                "p_report"
        );

        AiVerificationRequestedEvent event = eventBuilder.build(rawEvent);

        // 트랜잭션 커밋 이후 이벤트 발행
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            eventPublisher.publish(event);
                        }
                    }
            );
            return;
        }
        // 트랜잭션이 없는 경우 즉시 발행
        eventPublisher.publish(event);
    }
}

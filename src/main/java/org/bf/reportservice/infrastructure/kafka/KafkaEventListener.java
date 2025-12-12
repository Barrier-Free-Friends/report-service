package org.bf.reportservice.infrastructure.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bf.reportservice.domain.event.ReportCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventListener {

    @KafkaListener(
            topics = "report-events",
            groupId = "report-service-group",
            containerFactory = "genericKafkaListenerContainerFactory"
    )
    public void handlePointGain(ReportCreatedEvent event) {
        log.info("[ReportCreatedEvent] 수신 완료. 이벤트: {}", event);
    }
}

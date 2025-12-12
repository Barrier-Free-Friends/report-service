package org.bf.reportservice.kafka;

import org.bf.global.domain.event.DomainEventBuilder;
import org.bf.global.domain.event.EventPublisher;
import org.bf.reportservice.domain.event.ReportCreatedEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
public class KafkaTest {
    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private DomainEventBuilder eventBuilder;

    @Test
    void shouldPublishEventToRealKafkaBroker() throws InterruptedException {

        UUID userId = UUID.randomUUID();
        int point = 10;
        UUID sourceId = UUID.randomUUID();
        String sourceTable = "p_report";

        // 1. 순수 Payload 이벤트 생성
        ReportCreatedEvent rawEvent = new ReportCreatedEvent(userId, point, sourceId, sourceTable);

        // 2. DomainEventBuilder를 통해 메타데이터 주입
        ReportCreatedEvent event = eventBuilder.build(rawEvent);

        System.out.println("--- Publishing Kafka Event ---");
        System.out.println("Event ID: " + event.getEventId());
        System.out.println("Topic: " + event.getTopicName());
        System.out.println("Payload: " + event);
        System.out.println("------------------------------");

        eventPublisher.publish(event);

        // 비동기 전송 완료 대기
        Thread.sleep(3000);
    }
}

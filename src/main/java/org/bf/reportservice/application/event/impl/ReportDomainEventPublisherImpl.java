package org.bf.reportservice.application.event.impl;

import lombok.RequiredArgsConstructor;
import org.bf.global.domain.event.DomainEventBuilder;
import org.bf.global.domain.event.EventPublisher;
import org.bf.global.infrastructure.event.ReportCreatedEvent;
import org.bf.global.infrastructure.event.ReportDeletedEvent;
import org.bf.global.infrastructure.event.ReportMapImageInfo;
import org.bf.global.infrastructure.event.ReportMapInfoEvent;
import org.bf.reportservice.application.event.ReportDomainEventPublisher;
import org.bf.reportservice.application.event.dto.ReportImageLocation;
import org.bf.reportservice.domain.entity.ImageTag;
import org.bf.reportservice.domain.entity.Report;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReportDomainEventPublisherImpl implements ReportDomainEventPublisher {

    private final EventPublisher eventPublisher;
    private final DomainEventBuilder eventBuilder;

    /**
     * 제보 등록 도메인 이벤트 발행
     */
    @Override
    public void publishCreated(Report saved, ImageTag tag, List<ReportImageLocation> locations) {

        // Report 생성에 따른 포인트 지급 이벤트 발행
        ReportCreatedEvent rawEvent = new ReportCreatedEvent(
                saved.getUserId(),
                tag.getPoint(),
                saved.getId(),
                "p_report"
        );

        ReportCreatedEvent pointEvent = eventBuilder.build(rawEvent);
        eventPublisher.publish(pointEvent);

        saved.markPointRewarded();

        // 맵 서비스용 이미지 정보 구성
        List<ReportMapImageInfo> mapImages = locations.stream()
                .map(i -> new ReportMapImageInfo(i.latitude(), i.longitude()))
                .toList();

        // Report 생성에 따른 지도 반영 이벤트 발행
        ReportMapInfoEvent mapRawEvent = new ReportMapInfoEvent(
                saved.getUserId(),
                tag.getCode(),
                mapImages,
                saved.getId(),
                "p_report"
        );

        ReportMapInfoEvent mapEvent = eventBuilder.build(mapRawEvent);
        eventPublisher.publish(mapEvent);
    }

    @Override
    public void publishDeleted(Report report) {

        // 포인트 회수 처리를 위한 제보 삭제 도메인 이벤트 발행
        ReportDeletedEvent rawEvent = new ReportDeletedEvent(
                report.getUserId(),
                report.getId(),
                "p_report"
        );

        ReportDeletedEvent event = eventBuilder.build(rawEvent);
        eventPublisher.publish(event);
    }
}

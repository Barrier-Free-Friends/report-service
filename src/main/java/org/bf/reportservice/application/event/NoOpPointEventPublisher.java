package org.bf.reportservice.application.event;

import lombok.extern.slf4j.Slf4j;
import org.bf.reportservice.domain.entity.Report;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NoOpPointEventPublisher implements PointEventPublisher {

    @Override
    public void publishPointRevokeEvent(Report report) {
        // TODO: 카프카 연동
        log.info("point revoke event requested. reportId={}, userId={}",
                report.getId(), report.getUserId());
    }
}

package org.bf.reportservice.application.event;

import org.bf.reportservice.domain.entity.Report;

public interface PointEventPublisher {
    void publishPointRevokeEvent(Report report);
}

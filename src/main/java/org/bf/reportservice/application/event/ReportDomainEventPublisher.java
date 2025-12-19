package org.bf.reportservice.application.event;

import org.bf.reportservice.application.event.dto.ReportImageLocation;
import org.bf.reportservice.domain.entity.ImageTag;
import org.bf.reportservice.domain.entity.Report;

import java.util.List;

public interface ReportDomainEventPublisher {
    void publishCreated(Report saved, ImageTag tag, List<ReportImageLocation> locations);
    void publishDeleted(Report report);
}

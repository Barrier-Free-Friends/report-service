package org.bf.reportservice.application.event;

import org.bf.reportservice.application.event.dto.ReportImageLocation;
import org.bf.reportservice.domain.entity.ImageTag;
import org.bf.reportservice.domain.entity.Report;

import java.util.List;

public interface ReportDomainEventPublisher {
    /**
     * 제보 생성 이벤트 발행
     */
    void publishCreated(Report saved, ImageTag tag, List<ReportImageLocation> locations);

    /**
     * 제보 삭제 이벤트 발행
     */
    void publishDeleted(Report report);
}

package org.bf.reportservice.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bf.global.domain.event.AbstractDomainEvent;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportDeletedEvent extends AbstractDomainEvent {
    private UUID userId;
    private UUID sourceId;
    private String sourceTable;

    @Override
    public String getTopicName() {
        return "report-events";
    }
}

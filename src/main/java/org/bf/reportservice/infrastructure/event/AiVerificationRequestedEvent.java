package org.bf.reportservice.infrastructure.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bf.global.domain.event.AbstractDomainEvent;

import java.util.List;
import java.util.UUID;

/**
 * AI 이미지 검증 요청 이벤트
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AiVerificationRequestedEvent extends AbstractDomainEvent {

    private List<AiImageMeta> images;
    private UUID sourceId;
    private String sourceTable;

    @Override
    public String getTopicName() {
        return "ai-verification-request";
    }
}

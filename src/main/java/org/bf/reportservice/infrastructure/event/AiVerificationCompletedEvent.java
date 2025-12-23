package org.bf.reportservice.infrastructure.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bf.global.domain.event.AbstractDomainEvent;

import java.util.UUID;

/**
 * AI 이미지 검증 결과 전달 이벤트
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AiVerificationCompletedEvent extends AbstractDomainEvent {

    @JsonProperty("analysis_result")
    private String analysisResult;

    @JsonProperty("is_obstacle")
    private boolean isObstacle;

    private String tag;
    private UUID sourceId;
    private String sourceTable;

    @Override
    public String getTopicName() {
        return "ai-verification-result";
    }
}

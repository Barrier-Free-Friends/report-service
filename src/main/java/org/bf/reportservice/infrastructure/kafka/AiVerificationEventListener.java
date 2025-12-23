package org.bf.reportservice.infrastructure.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bf.reportservice.domain.entity.ImageTag;
import org.bf.reportservice.domain.entity.Report;
import org.bf.reportservice.domain.entity.VerificationStatus;
import org.bf.reportservice.domain.repository.ReportRepository;
import org.bf.reportservice.infrastructure.event.AiVerificationCompletedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AiVerificationEventListener {

    private final ReportRepository reportRepository;

    /**
     * AI 검증 완료 이벤트 수신
     * - sourceId(reportId)로 제보 조회
     * - 검증 상태가 PENDING인 경우에만 처리
     * - 검증 실패 시 결과만 반영 (상태 변경은 도메인 로직에 위임)
     * - 검증 성공 시 장애물 태그 설정 후 결과 반영
     */
    @KafkaListener(
            topics = "ai-verification-result",
            groupId = "report-service-group",
            containerFactory = "genericKafkaListenerContainerFactory"
    )
    @Transactional
    public void handle(AiVerificationCompletedEvent event) {

        // 검증 대상 제보 조회
        Report report = reportRepository.findById(event.getSourceId()).orElse(null);

        if (report == null) return;

        // 이미 검증이 끝난 제보인 경우 중복 처리 방지
        if (report.getVerificationStatus() != VerificationStatus.PENDING) {
            return;
        }

        // 장애물이 아닌 경우 태그 없이 검증 결과만 반영
        if (!event.isObstacle()) {
            report.updateVerificationResult(false, event.getAnalysisResult());
            return;
        }

        // 장애물인 경우 태그 설정 후 검증 결과 반영
        report.updateTag(ImageTag.from(event.getTag()));
        report.updateVerificationResult(true, event.getAnalysisResult());
    }
}

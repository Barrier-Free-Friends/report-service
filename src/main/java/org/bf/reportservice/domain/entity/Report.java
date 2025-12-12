package org.bf.reportservice.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bf.global.domain.Auditable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Table(name = "p_report")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Access(AccessType.FIELD)
public class Report extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ImageTag tag;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus reportStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationStatus verificationStatus;

    private String verificationMessage;

    @Column(nullable = false)
    private boolean isPointRewarded;

    // 일대다 관계 설정
    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ReportImage> images = new ArrayList<>();

    @Builder
    private Report(UUID userId, String title, String content, ImageTag tag) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.tag = tag;
        this.reportStatus = ReportStatus.PENDING;
        this.verificationStatus = VerificationStatus.PENDING;
        this.isPointRewarded = false;
    }

    /**
     * 이미지 추가
     */
    public void addImage(ReportImage image) {
        this.images.add(image);
        image.setReport(this);
    }

    /**
     * AI 검증 결과 반영
     * - 성공: APPROVED / SUCCESS
     * - 실패: REJECTED / FAILED
     */
    public void updateVerificationResult(boolean success, String message) {
        this.verificationMessage = message;
        if(success) {
            this.reportStatus = ReportStatus.APPROVED;
            this.verificationStatus = VerificationStatus.SUCCESS;
        } else {
            this.reportStatus = ReportStatus.REJECTED;
            this.verificationStatus = VerificationStatus.FAILED;
        }
    }

    /**
     * 제보글 제목 및 내용 수정
     */
    public void updateContent(String title, String content) {
        if (title != null && !title.isBlank()) {
            this.title = title;
        }
        if (content != null && !content.isBlank()) {
            this.content = content;
        }
    }

    /**
     * 제보글 소프트 삭제
     */
    public void delete(String username) {
        this.reportStatus = ReportStatus.DELETED;
        softDelete(username);
    }

    public boolean isDeleted() {
        return this.reportStatus == ReportStatus.DELETED;
    }

    /**
     * 포인트 지급 완료 상태 변경
     */
    public void markPointRewarded() {
        this.isPointRewarded = true;
    }
}

package org.bf.reportservice.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@Table(name = "p_report_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Access(AccessType.FIELD)
public class ReportImage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    @Column(nullable = false)
    private String fileUrl;

    private Double latitude;
    private Double longitude;
    private String address;

    @Builder
    public ReportImage(String fileUrl, Double latitude, Double longitude, String address) {
        this.fileUrl = fileUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    void setReport(Report report) {
        this.report = report;
    }

    public static ReportImage create(String fileUrl, Double latitude, Double longitude, String address) {
        return ReportImage.builder()
                .fileUrl(fileUrl)
                .latitude(latitude)
                .longitude(longitude)
                .address(address)
                .build();
    }
}

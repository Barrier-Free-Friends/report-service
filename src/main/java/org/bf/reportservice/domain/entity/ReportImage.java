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

    @Column(nullable = false, unique = true, length = 64)
    private String imageHash;

    @Builder
    public ReportImage(String fileUrl, String imageHash, Double latitude, Double longitude, String address) {
        this.fileUrl = fileUrl;
        this.imageHash = imageHash;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    void setReport(Report report) {
        this.report = report;
    }

    public static ReportImage create(String fileUrl, String imageHash, Double latitude, Double longitude, String address) {
        return ReportImage.builder()
                .fileUrl(fileUrl)
                .imageHash(imageHash)
                .latitude(latitude)
                .longitude(longitude)
                .address(address)
                .build();
    }
}

package org.bf.reportservice.application.event.dto;

/**
 * 제보 이미지 위치 정보
 */
public record ReportImageLocation(
        Double latitude,
        Double longitude
) {}

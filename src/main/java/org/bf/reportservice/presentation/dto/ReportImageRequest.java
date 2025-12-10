package org.bf.reportservice.presentation.dto;

public record ReportImageRequest(
        String fileUrl,
        Double latitude,
        Double longitude,
        String address
) {}

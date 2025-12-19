package org.bf.reportservice.presentation.dto;

import org.bf.reportservice.domain.entity.ImageTag;

import java.time.LocalDateTime;

public record ReportSearchRequest(
        String keyword,
        ImageTag tag,
        LocalDateTime from,
        LocalDateTime to
) {}

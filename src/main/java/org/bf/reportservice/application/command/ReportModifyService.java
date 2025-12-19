package org.bf.reportservice.application.command;

import org.bf.reportservice.presentation.dto.ReportDeleteResponse;
import org.bf.reportservice.presentation.dto.ReportResponse;
import org.bf.reportservice.presentation.dto.ReportUpdateRequest;

import java.util.UUID;

public interface ReportModifyService {
    ReportResponse update(UUID reportId, UUID userId, ReportUpdateRequest request);
    ReportDeleteResponse delete(UUID reportId, UUID requesterUserId);
}

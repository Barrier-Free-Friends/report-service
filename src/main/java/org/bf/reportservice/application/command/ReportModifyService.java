package org.bf.reportservice.application.command;

import org.bf.reportservice.presentation.dto.ReportDeleteResponse;
import org.bf.reportservice.presentation.dto.ReportResponse;
import org.bf.reportservice.presentation.dto.ReportUpdateRequest;

import java.util.UUID;

/**
 * 제보 수정, 삭제 서비스
 */
public interface ReportModifyService {
    /**
     * 제보 수정
     */
    ReportResponse update(UUID reportId, UUID userId, ReportUpdateRequest request);

    /**
     * 제보 삭제
     */
    ReportDeleteResponse delete(UUID reportId, UUID requesterUserId);
}

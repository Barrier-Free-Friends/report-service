package org.bf.reportservice.application;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bf.reportservice.application.command.ReportCreateService;
import org.bf.reportservice.application.command.ReportModifyService;
import org.bf.reportservice.presentation.dto.ReportCreateRequest;
import org.bf.reportservice.presentation.dto.ReportDeleteResponse;
import org.bf.reportservice.presentation.dto.ReportResponse;
import org.bf.reportservice.presentation.dto.ReportUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportCreateService reportCreateService;
    private final ReportModifyService reportModifyService;

    /**
     * 제보 등록
     */
    public ReportResponse createReport(@Valid ReportCreateRequest request, List<MultipartFile> files, UUID userId) {
        return reportCreateService.create(request, files, userId);
    }

    /**
     * 제보글 수정
     */
    public ReportResponse updateReport(UUID reportId, UUID userId, ReportUpdateRequest request) {
        return reportModifyService.update(reportId, userId, request);
    }

    /**
     * 제보글 삭제
     */
    public ReportDeleteResponse deleteReport(UUID reportId, UUID requesterUserId) {
        return reportModifyService.delete(reportId, requesterUserId);
    }
}

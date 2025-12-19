package org.bf.reportservice.application.command;

import jakarta.validation.Valid;
import org.bf.reportservice.presentation.dto.ReportCreateRequest;
import org.bf.reportservice.presentation.dto.ReportResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * 제보 등록 서비스
 */
public interface ReportCreateService {
    /**
     * 제보 등록
     */
    ReportResponse create(@Valid ReportCreateRequest request, List<MultipartFile> files, UUID userId);
}

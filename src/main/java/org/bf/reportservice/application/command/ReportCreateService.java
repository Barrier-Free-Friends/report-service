package org.bf.reportservice.application.command;

import jakarta.validation.Valid;
import org.bf.reportservice.presentation.dto.ReportCreateRequest;
import org.bf.reportservice.presentation.dto.ReportResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ReportCreateService {
    ReportResponse create(@Valid ReportCreateRequest request, List<MultipartFile> files, UUID userId);
}

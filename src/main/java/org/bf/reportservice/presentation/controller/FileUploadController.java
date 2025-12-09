package org.bf.reportservice.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.bf.reportservice.application.ImageUploadService;
import org.bf.reportservice.presentation.dto.FileUploadResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/upload")
public class FileUploadController {

    private final ImageUploadService imageUploadService;

    @PostMapping
    public ResponseEntity<FileUploadResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        String s3Url = imageUploadService.upload("images", file);
        return ResponseEntity.ok(new FileUploadResponse(s3Url));
    }
}
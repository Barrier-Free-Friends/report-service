package org.bf.reportservice.application;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadService {
    String upload(String key, MultipartFile file);
}

package org.bf.reportservice.application;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadService {
    /**
     * 이미지 업로드
     */
    String upload(String key, MultipartFile file);
}

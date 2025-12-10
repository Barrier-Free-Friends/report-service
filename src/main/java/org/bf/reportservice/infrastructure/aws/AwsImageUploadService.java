package org.bf.reportservice.infrastructure.aws;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.bf.global.infrastructure.exception.CustomException;
import org.bf.reportservice.application.ImageUploadService;
import org.bf.reportservice.infrastructure.aws.exception.AwsErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AwsImageUploadService implements ImageUploadService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public String upload(String key, MultipartFile file) {

        // 업로드될 파일명(경로) 생성
        String originalFileName = file.getOriginalFilename();
        String uniqueFileName = key + "/" + UUID.randomUUID() + "_" + originalFileName;

        // S3 업로드 메타데이터 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {

            // S3 업로드 요청 생성
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucket,
                    uniqueFileName,
                    inputStream,
                    metadata
            ).withCannedAcl(CannedAccessControlList.PublicRead);

            // S3 업로드 실행
            amazonS3Client.putObject(putObjectRequest);

            // 업로드된 파일의 public URL 반환
            return amazonS3Client.getUrl(bucket, uniqueFileName).toString();

        } catch (Exception e) {
            throw new CustomException(AwsErrorCode.FILE_UPLOAD_FAILED);
        }
    }
}

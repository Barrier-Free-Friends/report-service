package org.bf.reportservice.application.command.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bf.global.infrastructure.exception.CustomException;
import org.bf.reportservice.application.ImageUploadService;
import org.bf.reportservice.application.command.ReportCreateService;
import org.bf.reportservice.application.event.ReportDomainEventPublisher;
import org.bf.reportservice.application.event.dto.ReportImageLocation;
import org.bf.reportservice.domain.entity.ImageTag;
import org.bf.reportservice.domain.entity.Report;
import org.bf.reportservice.domain.entity.ReportImage;
import org.bf.reportservice.domain.exception.ReportErrorCode;
import org.bf.reportservice.domain.repository.ReportRepository;
import org.bf.reportservice.infrastructure.api.AiVerificationClient;
import org.bf.reportservice.infrastructure.api.dto.AiImageRequest;
import org.bf.reportservice.infrastructure.api.dto.AiVerificationResponse;
import org.bf.reportservice.presentation.dto.ReportCreateRequest;
import org.bf.reportservice.presentation.dto.ReportResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportCreateServiceImpl implements ReportCreateService {

    private final ReportRepository reportRepository;
    private final AiVerificationClient aiVerificationClient;
    private final ImageUploadService imageUploadService;
    private final ReportDomainEventPublisher reportDomainEventPublisher;

    /**
     * 제보 등록
     * 1) 요청 이미지 메타 및 파일 유효성 검증
     * 2) AI 서비스에 이미지 검증 요청
     * 3) 검증 성공 시 Report 및 ReportImage 엔티티 생성
     * 4) 검증 결과 및 상태 반영 후 저장
     * 5) 제보 등록에 따른 포인트 지급 / 지도 반영 이벤트 발행
     * 6) 응답 반환
     */
    @Transactional
    @Override
    public ReportResponse create(@Valid ReportCreateRequest request, List<MultipartFile> files, UUID userId) {

        // 이미지 메타 정보 존재 여부
        if (request == null || request.images() == null || request.images().isEmpty()) {
            throw new CustomException(ReportErrorCode.IMAGE_REQUIRED);
        }

        // 실제 업로드 파일 존재 여부
        if (files == null || files.isEmpty()) {
            throw new CustomException(ReportErrorCode.IMAGE_REQUIRED);
        }

        // 업로드 파일과 이미지 메타 정보의 1:1 매핑 검증
        if (request.images().size() != files.size()) {
            throw new CustomException(ReportErrorCode.INVALID_IMAGE_COUNT);
        }

        // 업로드 파일과 메타 정보를 순서대로 매핑하여 이미지 업로드 처리
        List<UploadedImage> uploaded = new ArrayList<>();

        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            var meta = request.images().get(i);

            String hash = sha256(file);
            String key = buildS3Key(userId, file);
            String url = imageUploadService.upload(key, file);

            uploaded.add(new UploadedImage(url, hash, meta.latitude(), meta.longitude(), meta.address()));
        }

        // 지도 반영 이벤트용 이미지 위치 정보 추출
        List<ReportImageLocation> locations = uploaded.stream()
                .map(i -> new ReportImageLocation(i.latitude(), i.longitude()))
                .toList();

        // AI 요청용 DTO 변환
        List<AiImageRequest> aiImages = uploaded.stream()
                .map(i -> new AiImageRequest(
                        i.fileUrl(),
                        i.latitude(),
                        i.longitude(),
                        i.address()
                ))
                .toList();

        // AI 검증 요청
        AiVerificationResponse ai;
        try {
            ai = aiVerificationClient.requestVerification(aiImages);
        } catch (Exception e) {
            throw new CustomException(ReportErrorCode.AI_SERVICE_ERROR);
        }

        // AI 응답 유효성
        if (ai == null) {
            throw new CustomException(ReportErrorCode.AI_SERVICE_ERROR);
        }

        log.info("반환값: {}", ai);

        // 검증 실패 시 등록 불가
        if (!ai.isObstacle()) {
            throw new CustomException(ReportErrorCode.AI_VERIFICATION_FAILED);
        }

        // 검증 성공 시 등록
        ImageTag tag = ImageTag.from(ai.tag());

        Report report = Report.builder()
                .userId(userId)
                .title(request.title())
                .content(request.content())
                .tag(tag)
                .build();

        uploaded.stream()
                .map(i -> ReportImage.create(
                        i.fileUrl(),
                        i.imageHash(),
                        i.latitude(),
                        i.longitude(),
                        i.address()
                ))
                .forEach(report::addImage);

        // 검증 결과 반영
        report.updateVerificationResult(true, ai.analysisResult());

        Report saved;
        try {
            saved = reportRepository.saveAndFlush(report);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ReportErrorCode.IMAGE_ALREADY_REGISTERED);
        }

        // 제보 등록에 따른 포인트 지급 / 지도 반영 이벤트 발행
        reportDomainEventPublisher.publishCreated(saved, tag, locations);

        return ReportResponse.from(saved);
    }

    public record UploadedImage(
            String fileUrl, String imageHash, Double latitude, Double longitude, String address
    ) {}

    /**
     * S3에 저장될 경로 생성
     */
    private String buildS3Key(UUID userId, MultipartFile file) {
        String ext = extractExt(file.getOriginalFilename());
        return "images/report/" + userId + "/" + UUID.randomUUID() + "." + ext;
    }

    /**
     * 확장자 추출
     */
    private String extractExt(String filename) {
        if (filename == null) return "jpg";
        int dot = filename.lastIndexOf('.');
        if (dot < 0 || dot == filename.length() - 1) return "jpg";
        return filename.substring(dot + 1).toLowerCase();
    }

    /**
     * 파일 내용으로 해시 생성
     */
    private String sha256(MultipartFile file) {
        try {
            var md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(file.getBytes());
            var sb = new StringBuilder();
            for (byte b : digest) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new CustomException(ReportErrorCode.FILE_HASH_FAILED);
        }
    }
}

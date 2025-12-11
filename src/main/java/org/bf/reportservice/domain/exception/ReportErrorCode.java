package org.bf.reportservice.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bf.global.infrastructure.error.BaseErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ReportErrorCode implements BaseErrorCode {

    IMAGE_REQUIRED(HttpStatus.BAD_REQUEST, "REPORT_IMAGE_REQUIRED_400", "제보글 등록을 위해서는 최소 한 장 이상의 이미지를 제출해야 합니다."),
    AI_VERIFICATION_FAILED(HttpStatus.BAD_REQUEST, "REPORT_AI_VERIFICATION_FAILED_400", "이미지 검증에 실패하여 제보글을 등록할 수 없습니다."),
    AI_SERVICE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "REPORT_AI_SERVICE_ERROR_500", "이미지 검증 서비스 호출 중 오류가 발생했습니다."),
    REPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "REPORT_NOT_FOUND_404", "존재하지 않는 제보글입니다."),
    REPORT_FORBIDDEN(HttpStatus.FORBIDDEN, "REPORT_FORBIDDEN_403", "다른 사람의 제보글을 수정할 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}

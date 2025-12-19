package org.bf.reportservice.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bf.global.infrastructure.error.BaseErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ReportErrorCode implements BaseErrorCode {

    // 제보 등록/처리 에러
    IMAGE_REQUIRED(HttpStatus.BAD_REQUEST, "REPORT_IMAGE_REQUIRED_400", "제보글 등록을 위해서는 최소 한 장 이상의 이미지를 첨부해야 합니다."),
    IMAGE_ALREADY_REGISTERED(HttpStatus.CONFLICT, "REPORT_IMAGE_ALREADY_REGISTERED_409", "이미 등록된 이미지입니다."),
    INVALID_IMAGE_COUNT(HttpStatus.BAD_REQUEST, "REPORT_INVALID_IMAGE_COUNT_400", "이미지 정보와 업로드된 파일 수가 일치하지 않습니다."),
    FILE_HASH_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "REPORT_FILE_HASH_FAILED_500", "이미지 처리 중 오류가 발생했습니다."),
    AI_VERIFICATION_FAILED(HttpStatus.BAD_REQUEST, "REPORT_AI_VERIFICATION_FAILED_400", "이미지 검증에 실패하여 제보글을 등록할 수 없습니다."),
    AI_SERVICE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "REPORT_AI_SERVICE_ERROR_500", "이미지 검증 서비스 호출 중 오류가 발생했습니다."),

    // 제보 접근/상태 에러
    REPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "REPORT_NOT_FOUND_404", "존재하지 않는 제보글입니다."),
    REPORT_FORBIDDEN(HttpStatus.FORBIDDEN, "REPORT_FORBIDDEN_403", "다른 사람의 제보글은 수정하거나 삭제할 수 없습니다."),
    REPORT_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "REPORT_ALREADY_DELETED_400", "이미 삭제된 제보글입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}

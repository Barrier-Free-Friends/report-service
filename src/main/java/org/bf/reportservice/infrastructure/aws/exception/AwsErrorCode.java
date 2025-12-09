package org.bf.reportservice.infrastructure.aws.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bf.global.infrastructure.error.BaseErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AwsErrorCode implements BaseErrorCode {

    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AWS_FILE_UPLOAD_FAILED_500", "이미지 업로드 중 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}

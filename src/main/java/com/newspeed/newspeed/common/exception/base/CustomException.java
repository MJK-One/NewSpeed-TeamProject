package com.newspeed.newspeed.common.exception.base;

import com.newspeed.newspeed.common.exception.code.enums.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage()); // %s 없음
        this.errorCode = errorCode;
    }

    public CustomException(ErrorCode errorCode, Object... args) {
        super(errorCode.getMessage(args)); // ← 동적 메시지
        this.errorCode = errorCode;
    }

    public HttpStatus getHttpStatus() {
        return errorCode.getHttpStatus();
    }
}

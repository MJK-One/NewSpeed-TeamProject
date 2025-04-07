package com.newspeed.newspeed.common.response; // API 응답의 공통 포맷을 정의하는 DTO 클래스


import com.newspeed.newspeed.common.exception.code.enums.ErrorCode;
import com.newspeed.newspeed.common.exception.code.enums.SuccessCode;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonInclude;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;



@Builder
public record ApiResponseDto<T>(
        LocalDateTime timestamp,                       // 요청 시각
        int statusCode,                                // HTTP 상태 코드 숫자 (예: 400)
        @NonNull String message,                       // 응답 메시지
        String path,                                   // 요청 경로
        @JsonInclude(value = NON_NULL) T data          // 실제 응답 데이터 (nullable), null이면 JSON에서 제외됨
) {

    // 성공 응답 (데이터 포함)
    public static <T> ApiResponseDto<T> success(final SuccessCode successCode, @Nullable final T data, final String path) {
        return ApiResponseDto.<T>builder()
                .timestamp(LocalDateTime.now())
                .statusCode(successCode.getHttpStatus().value())
                .message(successCode.getMessage())
                .path(path)
                .data(data)
                .build();
    }

    // 성공 응답 (데이터 없이)
    public static <T> ApiResponseDto<T> success(final SuccessCode successCode, final String path) {
        return success(successCode, null, path);
    }

    // 실패 응답 (ErrorCode 기반)
    public static <T> ApiResponseDto<T> fail(final ErrorCode errorCode, final String path) {
        return ApiResponseDto.<T>builder()
                .timestamp(LocalDateTime.now())
                .statusCode(errorCode.getHttpStatus().value())
                .message(errorCode.getMessage())
                .path(path)
                .data(null)
                .build();
    }

    // 실패 응답 (직접 코드와 메시지를 지정하는 경우)
    public static <T> ApiResponseDto<T> fail(final String message, final String path) {
        return ApiResponseDto.<T>builder()
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .path(path)
                .data(null)
                .build();
    }

}

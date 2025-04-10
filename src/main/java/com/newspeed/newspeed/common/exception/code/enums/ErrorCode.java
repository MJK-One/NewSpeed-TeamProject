package com.newspeed.newspeed.common.exception.code.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 400 - 잘못된 요청
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    MISSING_PARAMETER(HttpStatus.BAD_REQUEST, "필수 파라미터가 누락되었습니다."),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "유효하지 않은 요청 파라미터입니다."),

    // 405 - 지원하지 않는 메서드
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 메소드입니다."),

    // 비밀번호 관련
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    // 중복 이메일
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "이미 사용 중인 이메일입니다."),

    //친구 관련
    ALREADY_EXIST_FRIENDSHIP(HttpStatus.BAD_REQUEST, "이미 존재하는 친구 요청입니다. (상태: %s)"),
    NOT_ALLOW_REQUEST_MYSELF(HttpStatus.BAD_REQUEST,"자기 자신에게는 요청을 보낼 수 없습니다."),
    FRIEND_NOT_FOUND(HttpStatus.BAD_REQUEST,"존재하지 않는 친구 요청 ID입니다."),
    NOT_ALLOW_HANDLE_PENDING(HttpStatus.BAD_REQUEST,"요청 수락 혹은 거절만 가능합니다."),
    NOT_YOUR_REQUEST(HttpStatus.BAD_REQUEST,"사용자의 친구 요청이 아닙니다."),

    // 404 - Not Found
    NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 API입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다."),

    // 500 - 서버 내부 오류
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),
    TEST_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "테스트 에러입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}


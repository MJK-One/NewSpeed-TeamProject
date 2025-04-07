package com.newspeed.newspeed.common.exception.code.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode {

    // 201 Created
    USER_SIGNUP_SUCCESS(HttpStatus.CREATED, "회원가입이 완료되었습니다."),

    // 200 Ok
    GENERAL_SUCCESS(HttpStatus.OK, "테스트에 성공했습니다."),
    USER_LOGIN_SUCCESS(HttpStatus.OK, "로그인이 완료되었습니다.");


    private final HttpStatus httpStatus;
    private final String message;
}


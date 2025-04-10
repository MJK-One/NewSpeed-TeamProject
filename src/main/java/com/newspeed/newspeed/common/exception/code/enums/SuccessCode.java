package com.newspeed.newspeed.common.exception.code.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode {

    // 201 Created
    USER_SIGNUP_SUCCESS(HttpStatus.CREATED, "회원가입이 완료되었습니다."),
    REQUEST_FRIEND_SUCCESS(HttpStatus.CREATED, "친구 요청이 완료되었습니다"),

    // 200 Ok
    GENERAL_SUCCESS(HttpStatus.OK, "테스트에 성공했습니다."),
    USER_LOGIN_SUCCESS(HttpStatus.OK, "로그인이 완료되었습니다."),

    PROFILE_VIEW_SUCCESS(HttpStatus.OK, "프로필 조회에 성공했습니다."),
    USER_UPDATE_SUCCESS(HttpStatus.OK, "유저 정보 업데이트가 완료되었습니다."),

    HANDLE_FRIEND_SUCCESS(HttpStatus.OK, "친구 요청에 대한 응답이 완료되었습니다"),
    GET_FRIENDSHIPS_SUCCESS(HttpStatus.OK, "친구 목록 조회가 성공하였습니다"),
    GET_FRIENDSHIPREQUESTS_SUCCESS(HttpStatus.OK, "친구 요청 목록 조회가 성공하였습니다");


    private final HttpStatus httpStatus;
    private final String message;
}


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
    USER_LOGIN_SUCCESS(HttpStatus.OK, "로그인이 완료되었습니다."),

    //게시글 관련
    CREATE_POST(HttpStatus.CREATED, "게시글 작성 완료"),
    GET_POST(HttpStatus.OK, "게시글 조회 성공"),
    GET_POSTS(HttpStatus.OK, "뉴스피드 조회 성공"),
    UPDATE_POST(HttpStatus.OK, "게시글 수정 완료"),
    DELETE_POST(HttpStatus.NO_CONTENT, "게시글 삭제 완료"),
    TOGGLE_LIKE(HttpStatus.OK, "좋아요 상태 변경");


    private final HttpStatus httpStatus;
    private final String message;
}


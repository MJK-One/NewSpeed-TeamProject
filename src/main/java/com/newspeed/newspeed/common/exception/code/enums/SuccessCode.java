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
    COMMENT_CREATE_SUCCESS(HttpStatus.CREATED, "댓글이 성공적으로 생성되었습니다."),

    // 200 Ok
    GENERAL_SUCCESS(HttpStatus.OK, "테스트에 성공했습니다."),
    USER_LOGIN_SUCCESS(HttpStatus.OK, "로그인이 완료되었습니다."),
    HANDLE_FRIEND_SUCCESS(HttpStatus.OK, "친구 요청에 대한 응답이 완료되었습니다"),
    GET_FRIENDSHIPS_SUCCESS(HttpStatus.OK, "친구 목록 조회가 성공하였습니다"),
    GET_FRIENDSHIPREQUESTS_SUCCESS(HttpStatus.OK, "친구 요청 목록 조회가 성공하였습니다"),
    COMMENT_UPDATE_SUCCESS(HttpStatus.OK, "댓글이 성공적으로 수정되었습니다."),
    COMMENT_LIKE_SUCCESS(HttpStatus.OK, "댓글 좋아요 성공"),
    COMMENT_UNLIKE_SUCCESS(HttpStatus.OK, "댓글 좋아요 취소 성공"),
    COMMENT_READ_SUCCESS(HttpStatus.OK, "댓글 조회 성공"),

    // 204 No Content
    COMMENT_DELETE_SUCCESS(HttpStatus.NO_CONTENT, "댓글이 성공적으로 삭제되었습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
package com.newspeed.newspeed.domain.users.controller;

import com.newspeed.newspeed.common.exception.base.CustomException;
import com.newspeed.newspeed.common.exception.code.enums.ErrorCode;
import com.newspeed.newspeed.common.exception.code.enums.SuccessCode;
import com.newspeed.newspeed.common.response.ApiResponseDto;
import com.newspeed.newspeed.domain.users.dto.request.UserLoginRequestDto;
import com.newspeed.newspeed.domain.users.dto.request.UserSignupRequestDto;
import com.newspeed.newspeed.domain.users.dto.request.UserWithdrawRequestDto;
import com.newspeed.newspeed.domain.users.entity.User;
import com.newspeed.newspeed.domain.users.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto<Void>> createUser(
            @RequestBody @Valid final UserSignupRequestDto userSignupRequestDto,
            HttpServletRequest httpServletRequest) {
        userService.signup(userSignupRequestDto, httpServletRequest);
        return ResponseEntity.status(201)
                .body(ApiResponseDto.success(SuccessCode.USER_SIGNUP_SUCCESS, "/api/users/singnup"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<Void>> login(
            @RequestBody @Valid final UserLoginRequestDto userLoginRequestDto,
            HttpServletRequest httpServletRequest) {
        userService.login(userLoginRequestDto, httpServletRequest);
        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.USER_LOGIN_SUCCESS, "/api/users/login"));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(
            @RequestBody @Valid UserWithdrawRequestDto requestDto,
            HttpServletRequest httpServletRequest) {

        HttpSession session = httpServletRequest.getSession(false);

        // 세션에서 유저 꺼내기
        User user = (User) session.getAttribute("user");

        // 탈퇴 처리
        userService.deleteUser(user.getUserId(), requestDto);

        // 세션 무효화 (로그아웃 처리)
        session.invalidate();

        // 204 No Content
        return ResponseEntity.noContent().build();
    }


}

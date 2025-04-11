package com.newspeed.newspeed.domain.users.controller;

import com.newspeed.newspeed.common.exception.code.enums.SuccessCode;
import com.newspeed.newspeed.common.response.ApiResponseDto;
import com.newspeed.newspeed.domain.users.dto.request.UserLoginRequestDto;
import com.newspeed.newspeed.domain.users.dto.request.UserSignupRequestDto;
import com.newspeed.newspeed.domain.users.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}

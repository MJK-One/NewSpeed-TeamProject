package com.newspeed.newspeed.domain.profiles.controller;

import com.newspeed.newspeed.common.exception.code.enums.SuccessCode;
import com.newspeed.newspeed.common.response.ApiResponseDto;
import com.newspeed.newspeed.domain.profiles.dto.OtherProfileResponseDto;
import com.newspeed.newspeed.domain.profiles.dto.ProfileRequestDto;
import com.newspeed.newspeed.domain.profiles.dto.MyProfileResponseDto;
import com.newspeed.newspeed.domain.profiles.service.ProfileService;
import com.newspeed.newspeed.domain.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/profiles")
@RequiredArgsConstructor
@RestController
public class ProfileController {

    private final ProfileService profileService;

    //프로필 조회
    @GetMapping("/{userId}")
    public ApiResponseDto<OtherProfileResponseDto> findProfileById(
            @PathVariable Long userId,
            @SessionAttribute(value = "loginUser") User user
            //TODO: 세션명 통일, 세션에 뭐가 저장됐는지 확인하고 User 바꿔야 함
    ) {
        return ApiResponseDto.success(SuccessCode.GENERAL_SUCCESS, profileService.findProfileById(userId, user.getId()), "/api/profiles/"+userId);
    }

    //내 프로필 조회
    @GetMapping("/my")
    public ApiResponseDto<MyProfileResponseDto> findMyProfileById(
            @SessionAttribute(value = "loginUser") User user
    ) {
        return ApiResponseDto.success(SuccessCode.GENERAL_SUCCESS, profileService.findMyProfileById(user.getId()), "/api/profiles/my");
    }

    //프로필 수정
    @PatchMapping
    public ApiResponseDto<Void> updateProfile(
            @RequestBody ProfileRequestDto requestDto,
            @SessionAttribute(value = "loginUser") User user
    ){
        return profileService.updateProfile(requestDto, user.getId());
    }
}

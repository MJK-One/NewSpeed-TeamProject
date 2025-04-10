package com.newspeed.newspeed.domain.profiles.controller;

import com.newspeed.newspeed.common.exception.code.enums.ErrorCode;
import com.newspeed.newspeed.common.exception.code.enums.SuccessCode;
import com.newspeed.newspeed.common.response.ApiResponseDto;
import com.newspeed.newspeed.domain.profiles.dto.response.OtherProfileResponseDto;
import com.newspeed.newspeed.domain.profiles.dto.request.ProfileUpdateRequestDto;
import com.newspeed.newspeed.domain.profiles.dto.response.MyProfileResponseDto;
import com.newspeed.newspeed.domain.profiles.service.ProfileService;
import com.newspeed.newspeed.domain.users.entity.User;
import jakarta.validation.Valid;
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
            @SessionAttribute(value = "user") User user
    ) {
        return ApiResponseDto.success(SuccessCode.PROFILE_VIEW_SUCCESS, profileService.findProfileById(userId, user.getUserId()), "/api/profiles/"+userId);
    }

    //내 프로필 조회
    @GetMapping("/my")
    public ApiResponseDto<MyProfileResponseDto> findMyProfileById(
            @SessionAttribute(value = "user") User user
    ) {
        return ApiResponseDto.success(SuccessCode.PROFILE_VIEW_SUCCESS, profileService.findMyProfileById(user.getUserId()), "/api/profiles/my");
    }

    //프로필 수정
    @PatchMapping
    public ApiResponseDto<Void> updateProfile(
            @RequestBody @Valid ProfileUpdateRequestDto requestDto,
            @SessionAttribute(value = "user") User user
    ){
        boolean updated = profileService.updateProfile(requestDto, user.getUserId());

        if(updated){
            return ApiResponseDto.success(SuccessCode.USER_UPDATE_SUCCESS, "/api/profiles");
        } else {
            return ApiResponseDto.fail(ErrorCode.INVALID_PASSWORD, "/api/profiles");
        }
    }
}

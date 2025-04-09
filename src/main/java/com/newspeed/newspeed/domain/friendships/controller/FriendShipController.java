package com.newspeed.newspeed.domain.friendships.controller;

import com.newspeed.newspeed.common.exception.code.enums.SuccessCode;
import com.newspeed.newspeed.common.response.ApiResponseDto;
import com.newspeed.newspeed.domain.friendships.dto.request.HandleFriendShipRequest;
import com.newspeed.newspeed.domain.friendships.dto.request.SendFriendShipRequest;
import com.newspeed.newspeed.domain.friendships.service.FriendShipService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.newspeed.newspeed.common.exception.code.enums.SuccessCode.HANDLE_FRIEND_SUCCESS;
import static com.newspeed.newspeed.common.exception.code.enums.SuccessCode.REQUEST_FRIEND_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friendships")
public class FriendShipController {

    private final FriendShipService friendShipService;

    @PostMapping
    public ApiResponseDto<Void> sendRequest(@RequestBody @Validated SendFriendShipRequest request) {
        //todo: 로그인 내용 Pull 이후 세션으로부터 유저 ID를 받아오는 로직 필요
        Long userId = 1L;
        friendShipService.sendRequest(request,userId);
        return ApiResponseDto.success(REQUEST_FRIEND_SUCCESS, "/api/friendships");
    }

    @PatchMapping("/{requestId}")
    public ApiResponseDto<Void> handleRequest(@PathVariable Long requestId, @RequestBody @Validated HandleFriendShipRequest request) {
        //todo: 로그인 내용 Pull 이후 세션으로부터 유저 ID를 받아오는 로직 필요
        Long userId = 1L;
        friendShipService.handleRequest(userId, requestId, request);
        return ApiResponseDto.success(HANDLE_FRIEND_SUCCESS, "/api/friendships");
    }
}

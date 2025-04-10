package com.newspeed.newspeed.domain.profiles.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class MyProfileResponseDto {

    private final MyProfileUserDto myProfileUserDto;
    private final List<ProfilePostDto> postList;

}


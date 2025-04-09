package com.newspeed.newspeed.domain.profiles.dto;

import lombok.Getter;
import java.util.List;

@Getter
public class MyProfileResponseDto {

    private final MyProfileUserDto myProfileUserDto;
    private final List<ProfilePostDto> postList;

    public MyProfileResponseDto(MyProfileUserDto myProfileUserDto, List<ProfilePostDto> postList) {
        this.myProfileUserDto = myProfileUserDto;
        this.postList = postList;
    }
}


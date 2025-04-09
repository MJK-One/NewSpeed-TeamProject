package com.newspeed.newspeed.domain.profiles.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class OtherProfileResponseDto {

    private final OtherProfileUserDto otherProfileUserDto;
    private final List<ProfilePostDto> postList;

    public OtherProfileResponseDto(OtherProfileUserDto otherProfileUserDto, List<ProfilePostDto> postList) {
        this.otherProfileUserDto = otherProfileUserDto;
        this.postList = postList;
    }
}


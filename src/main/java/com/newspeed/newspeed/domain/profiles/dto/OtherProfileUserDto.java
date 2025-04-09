package com.newspeed.newspeed.domain.profiles.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OtherProfileUserDto {
    private final long id;
    private final String name;
    private final String friendStatus;
    private final int postCount;
    private final int friendCount;
}



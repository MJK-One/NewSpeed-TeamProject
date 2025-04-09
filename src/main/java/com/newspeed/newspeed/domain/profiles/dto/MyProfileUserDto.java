package com.newspeed.newspeed.domain.profiles.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MyProfileUserDto {
    private final long id;
    private final String name;
    private final String email;
    private final int postCount;
    private final int friendCount;
}

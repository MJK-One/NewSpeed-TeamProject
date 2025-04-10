package com.newspeed.newspeed.domain.profiles.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MyProfileUserDto {
    private final Long id;
    private final String name;
    private final String email;
    private final int postCount;
    private final int friendCount;
}

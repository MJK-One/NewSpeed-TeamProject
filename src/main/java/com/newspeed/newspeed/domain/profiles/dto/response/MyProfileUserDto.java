package com.newspeed.newspeed.domain.profiles.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class MyProfileUserDto {
    private final Long id;
    private final String name;
    private final String email;
    private final int postCount;
    private final int friendCount;
}

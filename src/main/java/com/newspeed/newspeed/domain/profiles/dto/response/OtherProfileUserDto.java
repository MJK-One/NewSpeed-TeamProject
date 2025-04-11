package com.newspeed.newspeed.domain.profiles.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class OtherProfileUserDto {
    private final String name;
    private final String friendStatus;
    private final int postCount;
    private final int friendCount;
}



package com.newspeed.newspeed.domain.profiles.dto;

import lombok.Getter;

@Getter
public class ProfileRequestDto {

    private final String userName;
    private final String email;
    private final String newPassword;
    private final String previousPassword;

    public ProfileRequestDto(String userName, String email, String newPassword, String previousPassword) {
        this.userName = userName;
        this.email = email;
        this.newPassword = newPassword;
        this.previousPassword = previousPassword;
    }
}

package com.newspeed.newspeed.domain.profiles.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ProfileRequestDto {

    @Size(max = 4, message = "최대 4글자까지 가능합니다.")
    private final String userName;
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "올바른 이메일 형식이 아닙니다."
    )
    private final String email;
    @Size(min = 8, message = "비밀번호는 최소 8글자 이상이어야 합니다")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=-]).{8,}$",
            message = "비밀번호는 대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함되어야 합니다."
    )
    private final String newPassword;
    private final String previousPassword;

    public ProfileRequestDto(String userName, String email, String newPassword, String previousPassword) {
        this.userName = userName;
        this.email = email;
        this.newPassword = newPassword;
        this.previousPassword = previousPassword;
    }
}

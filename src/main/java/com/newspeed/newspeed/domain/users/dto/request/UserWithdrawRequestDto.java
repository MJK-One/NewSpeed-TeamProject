package com.newspeed.newspeed.domain.users.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserWithdrawRequestDto(
        @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
        @Size(min = 8, message = "비밀번호는 최소 8글자 이상이어야 합니다")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=-]).{8,}$",
                message = "비밀번호는 대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함되어야 합니다."
        )
        String password
) {
}

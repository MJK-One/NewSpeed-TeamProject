package com.newspeed.newspeed.domain.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class PostRequestDto {
    @NotBlank(message = "내용은 필수입니다.")
    private String content;
    private String image;
}

package com.newspeed.newspeed.domain.comments.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Valid
@Data
// 댓글 수정 요청 DTO
public class UpdateCommentRequestDto {
    @NotBlank(message = "댓글 내용은 필수입니다.")
    @Size(min = 1, max = 500, message = "댓글 내용은 1자 이상 500자 이하로 입력해야 합니다.")
    private String commentText; // 댓글
}


package com.newspeed.newspeed.domain.comments.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Valid
@Data
// 댓글 작성 요청 DTO
public class CommentRequestDto {
    @NotNull(message = "댓글 내용은 필수입니다.")
    private String commentText; // 댓글
}

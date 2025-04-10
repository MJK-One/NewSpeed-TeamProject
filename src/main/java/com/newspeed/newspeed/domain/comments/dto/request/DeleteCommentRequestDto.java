package com.newspeed.newspeed.domain.comments.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Valid
@Data
// 댓글 삭제 요청 DTO
public class DeleteCommentRequestDto {
    @NotNull(message = "댓글 ID는 필수입니다.")
    private Long commentId; // 댓글 ID
}

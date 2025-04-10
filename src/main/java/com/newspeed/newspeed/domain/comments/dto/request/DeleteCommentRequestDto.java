package com.newspeed.newspeed.domain.comments.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
// 댓글 삭제 요청 DTO
public class DeleteCommentRequestDto {
    private Long commentId; // 댓글 ID
}

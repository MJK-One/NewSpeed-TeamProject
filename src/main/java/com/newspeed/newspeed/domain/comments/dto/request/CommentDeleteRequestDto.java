package com.newspeed.newspeed.domain.comments.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
// 댓글 삭제 요청 DTO
public class CommentDeleteRequestDto {
    private Long commentId; // 댓글 ID
}

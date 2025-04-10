package com.newspeed.newspeed.domain.comments.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
// 댓글 조회 반환 DTO
public class CommentResponseDto {
    private Long commentId; // 댓글 ID

    private Long postId; // 게시글 ID

    private Long userId; // 작성자 ID

    private String username; // 작성자 이름

    private String commentText; // 댓글 내용

    private Integer commentLikes; // 댓글 좋아요 갯수
}

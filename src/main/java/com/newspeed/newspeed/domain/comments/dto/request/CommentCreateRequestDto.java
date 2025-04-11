package com.newspeed.newspeed.domain.comments.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
// 댓글 작성 요청 DTO
public class CommentCreateRequestDto {
    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String commentText; // 댓글
}
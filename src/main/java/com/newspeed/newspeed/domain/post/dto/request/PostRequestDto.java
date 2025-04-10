package com.newspeed.newspeed.domain.post.dto.request;

import com.newspeed.newspeed.domain.post.entity.Post;
import com.newspeed.newspeed.domain.post.entity.User;
import jakarta.validation.constraints.NotBlank;


public record PostRequestDto(
        @NotBlank(message = "내용은 필수입니다.")
        String content,
        String image
) {
    public Post toEntity(User user) {
        return Post.builder()
                .user(user)
                .content(this.content)
                .image(this.image)
                .likeCount(0)
                .build();
    }
}
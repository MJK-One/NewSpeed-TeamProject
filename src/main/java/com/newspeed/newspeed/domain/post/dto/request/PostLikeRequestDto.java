package com.newspeed.newspeed.domain.post.dto.request;

import com.newspeed.newspeed.domain.post.entity.Post;
import com.newspeed.newspeed.domain.post.entity.PostLike;
import com.newspeed.newspeed.domain.post.entity.User;

public record PostLikeRequestDto(Long userId, Long postId) {
    public PostLike toEntity(User user, Post post) {
        return PostLike.builder()
                .user(user)
                .post(post)
                .build();
    }
}
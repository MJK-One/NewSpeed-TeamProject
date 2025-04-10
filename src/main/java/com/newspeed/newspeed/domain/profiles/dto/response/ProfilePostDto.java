package com.newspeed.newspeed.domain.profiles.dto.response;

import com.newspeed.newspeed.domain.comments.entity.Post;
//TODO: 이 import 제대로 변경
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProfilePostDto {

    private final long postId;
    private final String postImage;
    private final int postLikes;
    private final int commentCount;

    public static ProfilePostDto toDto(Post post, int commentCount) {
        return new ProfilePostDto(post.getId(), post.getImage(), post.getLikes(), commentCount);
    }
}
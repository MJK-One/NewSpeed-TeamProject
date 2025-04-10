package com.newspeed.newspeed.domain.profiles.dto;

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
        return new ProfilePostDto(post.getId(), post.getImage(), post.getLikeCount(), commentCount);
    }
}
package com.newspeed.newspeed.domain.post.dto.response;

import com.newspeed.newspeed.domain.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class PostResponseDto {
    private Long id;
    private String content;
    private String image;
    private int likeCount;
    private String name;
    private LocalDateTime createdAt;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.content = post.getContent();
        this.image = post.getImage();
        this.likeCount = post.getLikeCount();
        this.name = post.getUser().getName();
        this.createdAt = post.getCreatedAt();
    }

    public static PostResponseDto from(Post post) {
        return PostResponseDto.builder()
                .id(post.getId())
                .content(post.getContent())
                .image(post.getImage())
                .likeCount(post.getLikeCount())
                .name(post.getUser().getName())
                .createdAt(post.getCreatedAt())
                .build();
    }
}

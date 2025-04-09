package com.newspeed.newspeed.domain.post.dto.response;

import java.time.LocalDateTime;

public class PostResponseDto {
    private Long id;
    private String content;
    private String image;
    private int likeCount;
    private String username;
    private LocalDateTime createdAt;
}

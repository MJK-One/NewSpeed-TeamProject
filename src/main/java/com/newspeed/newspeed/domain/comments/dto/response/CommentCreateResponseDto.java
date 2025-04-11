package com.newspeed.newspeed.domain.comments.dto.response;

import jdk.jshell.Snippet;
import lombok.*;

@Getter
@Builder
public class CommentCreateResponseDto {
    private final Long commentId;
    private final Long userId;
    private final Long postId;
    private final String commentText;
    private final int commentLikes;
}
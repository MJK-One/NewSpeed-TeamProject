package com.newspeed.newspeed.domain.comments.dto.response;

import lombok.*;

@Getter
@RequiredArgsConstructor
public class CreateCommentResponseDto {
    private final Long commentId;
    private final Long userId;
    private final Long postId;
    private final String commentText;
    private final int commentLikes;
}

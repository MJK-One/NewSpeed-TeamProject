package com.newspeed.newspeed.domain.comments.repository;

import com.newspeed.newspeed.domain.comments.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    void deleteByCommentIdAndUser_UserId(Long commentId, Long userId);
    boolean existsByCommentIdAndUser_UserId(Long commentId, Long userId);
}


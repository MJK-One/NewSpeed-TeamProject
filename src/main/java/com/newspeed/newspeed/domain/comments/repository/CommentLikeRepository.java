package com.newspeed.newspeed.domain.comments.repository;

import com.newspeed.newspeed.domain.comments.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    void deleteByComment_IdAndUser_Id(Long commentId, Long userId);
    boolean existsByComment_IdAndUser_Id(Long commentId, Long userId);
}


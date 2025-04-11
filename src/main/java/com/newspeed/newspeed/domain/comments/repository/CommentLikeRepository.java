package com.newspeed.newspeed.domain.comments.repository;

import com.newspeed.newspeed.domain.comments.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    @Modifying // 추가: DELETE, UPDATE 쿼리에는 @Modifying 어노테이션을 사용해야 합니다.
    @Transactional // 추가: 트랜잭션 처리를 위해 @Transactional 어노테이션을 추가합니다.
    @Query("DELETE FROM CommentLike cl WHERE cl.comment.id = :commentId AND cl.user.userId = :userId")
    void deleteByCommentIdAndUserId(@Param("commentId") Long commentId, @Param("userId") Long userId);

    @Query("SELECT COUNT(cl) > 0 FROM CommentLike cl WHERE cl.comment.id = :commentId AND cl.user.userId = :userId")
    boolean existsByCommentIdAndUserId(@Param("commentId") Long commentId, @Param("userId") Long userId);
}


package com.newspeed.newspeed.domain.comments.repository;

import com.newspeed.newspeed.domain.comments.dto.response.CommentResponseDto;
import com.newspeed.newspeed.domain.comments.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT " +
            "new com.newspeed.newspeed.domain.comments.dto.response.CommentResponseDto(" +
            "c.id, " +
            "u.userId, " +
            "u.name, " +
            "c.commentText, " +
            "c.commentLikes) " +
            "FROM Comment c " +
            "JOIN c.user u " +
            "WHERE c.post.id = :postId " +
            "ORDER BY c.id DESC")
    List<CommentResponseDto> findCommentResponseDtoByPostId(@Param("postId") Long postId);
}

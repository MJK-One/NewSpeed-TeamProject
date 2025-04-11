package com.newspeed.newspeed.domain.post.repository;

import com.newspeed.newspeed.domain.post.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

        // 유저가 이미 이 게시글에 좋아요 했는지 확인 (중복 방지)
        @Query("SELECT pl FROM PostLike pl WHERE pl.user.id = :userId AND pl.post.id = :postId")
        Optional<PostLike> findByUserIdAndPostId(@Param("userId") Long userId, @Param("postId") Long postId);

        // 존재 여부만 확인 (boolean 반환)
        @Query("SELECT COUNT(pl) > 0 FROM PostLike pl WHERE pl.user.id = :userId AND pl.post.id = :postId")
        boolean existsByUserIdAndPostId(@Param("userId") Long userId, @Param("postId") Long postId);

        // 좋아요 취소 (delete)
        @Modifying
        @Query("DELETE FROM PostLike pl WHERE pl.user.id = :userId AND pl.post.id = :postId")
        void deleteByUserIdAndPostId(@Param("userId") Long userId, @Param("postId") Long postId);


}

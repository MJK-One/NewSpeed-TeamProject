package com.newspeed.newspeed.domain.post.repository;

import com.newspeed.newspeed.domain.post.entity.Post;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    //게시물 단건 조회
    @Query("select p from Post p where p.id = :id")
    Optional<Post> findById(@Param("id") Long id);

    //작성자 본인만 수정 가능
    @Query("select p from Post p where p.id = :postId")
    Optional<Post> findByPostId(@Param("postId") Long postId);

    //최신순, 페이지네이션
    @Query(value = "select p from Post p order by p.createdAt desc",
            countQuery = "select count (p) from Post p")
    Page<Post> findNewsFeed(Pageable pageable);

    @Query("select p from Post p where p.id = :postId and p.user.id = :userId")
    Optional<Post> findByIdAndUserId(@Param("postId") Long id,@Param("userId") Long userId);

    // 시작일 조건이 없으면 필터 안함, 있으면 이후 조회
    // 종료일 조건 필터 안하고 있으면 이전까지 조회
    // 정렬 페이징 조건 자동 적용되는 Pageable
    @Query("SELECT p FROM Post p WHERE " +
            "(:start IS NULL OR p.updatedAt>= :start) AND " +
            "(:end IS NULL OR p.updatedAt < :end)")
    Page<Post> findPostsByModifiedAtRange(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable);
}

package com.newspeed.newspeed.domain.post.repository;

import com.newspeed.newspeed.domain.post.entity.Post;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


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
            countQuery= "select count (p) from Post p" )
    Page<Post> findNewsFeed(Pageable pageable);

    @Query("select p from Post p where p.id = :postId and p.user.id = :userId")
    Optional<Post> findByIdAndUserId(@Param("postId") Long postId, @Param("userId") Long userId);

}

package com.newspeed.newspeed.domain.post.entity;

import org.junit.jupiter.api.Test;

import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class PostTest {

    @Test
    void post() {
        //given
        User user = User.builder()
                .username("tester")
                .build();
        Post post = Post.createPost(user, "테스트 게시글", "image.png");
        PostLike postLike = PostLike.of(user, post);

        //when
        assertThat(postLike.getUser()).isEqualTo(user);
        assertThat(postLike.getPost()).isEqualTo(post);
        assertThat(post.getPostLikes()).doesNotContain(postLike);

        post.getPostLikes().add(postLike);
        assertThat(post.getPostLikes()).contains(postLike);
    }

}
package com.newspeed.newspeed.domain.post.entity;

import com.newspeed.newspeed.domain.post.dto.request.PostLikeRequestDto;
import org.junit.jupiter.api.Test;

import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class PostTest {

    @Test
    void post() {
        //given
        User user = User.builder()
                .name("tester")
                .build();
        Post post = Post.builder()
                .user(user)
                .content("test contents")
                .image("image.png")
                .likeCount(0)
                .build();

        PostLike postLike =  new PostLikeRequestDto(user.getId(), post.getId()).toEntity(user, post);


        //when
        assertThat(postLike.getUser()).isEqualTo(user);
        assertThat(postLike.getPost()).isEqualTo(post);

    }

}
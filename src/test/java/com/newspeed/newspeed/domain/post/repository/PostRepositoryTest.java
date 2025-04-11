package com.newspeed.newspeed.domain.post.repository;

import com.newspeed.newspeed.domain.post.entity.Post;

import com.newspeed.newspeed.domain.users.entity.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private EntityManager em;

    @Test
    void testFindByPostId() {
        //given
        User user = User.builder()
                .name("testuser")
                .build();
        em.persist(user);

        Post post = Post.builder()
                .user(user)
                .content("test contents")
                .image("image.png")
                .likeCount(0)
                .build();

        postRepository.save(post);

        //when
        Optional<Post> postOptional = postRepository.findById(post.getId());

        //then
        assertThat(postOptional).isPresent();
        assertThat(postOptional.get().getContent()).isEqualTo("test contents");
    }
   // @Test
   // @DisplayName("페이징이 적용 됐는지 테스트")
//    void testFindByNewsFeedPaging() {
//        //given
//        User user = User.builder()
//                .name("testuser2")
//                .build();
//
//        em.persist(user);
//
//        // 게시글 25개 저장
//        for ( int i = 0; i < 25; i++ ) {
//            Post post = Post.createPost(user, "test contents", "image.png");
//            em.persist(post);
//        }
//        em.flush();
//        em.clear();
//
//        // when : 0번째 페이지, 페이지당 10개 요청
//        Pageable pageable = PageRequest.of(0, 10);
//        Page<Post> page = postRepository.findNewsFeed(pageable, s);
//
//        // then
//        assertThat(page.getContent()).hasSize(10); // 실제 데이터 10개
//        assertThat(page.getTotalElements()).isEqualTo(25); // 총 25개
//        assertThat(page.getTotalPages()).isEqualTo(3); // 10개씩 3페이지
//        assertThat(page.getNumber()).isEqualTo(0); // 요청한 페이지 번호
//    }
}
package com.newspeed.newspeed.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newspeed.newspeed.domain.post.dto.request.PostRequestDto;
import com.newspeed.newspeed.domain.post.entity.Post;
import com.newspeed.newspeed.domain.post.entity.User;
import com.newspeed.newspeed.domain.post.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PostControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private EntityManager em;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;

    private User user;
    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        user = User.builder().name("testuser").build();
        em.persist(user);

        session = new MockHttpSession();
        session.setAttribute("userId", user.getId());

        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("게시글 생성 통합 테스트")
    void createPost() throws Exception {
        PostRequestDto requestDto = new PostRequestDto("테스트 게시글", "test.png");

        mockMvc.perform(post("/api/posts")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").value("테스트 게시글"))
                .andExpect(jsonPath("$.data.image").value("test.png"))
                .andExpect(jsonPath("$.data.username").value("testuser"));
    }

    @Test
    @DisplayName("뉴스피드 조회 페이징 테스트")
    void getNewsFeed() throws Exception {
        for (int i = 1; i <= 12; i++) {
            Post post = Post.builder()
                    .user(user)
                    .content("내용 " + i)
                    .image(null)
                    .likeCount(0)
                    .build();
            em.persist(post);
        }

        mockMvc.perform(get("/api/posts?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(10));
    }

    @Test
    @DisplayName("게시글 단건 조회 테스트")
    void getPost() throws Exception {
        Post post = Post.builder()
                .user(user)
                .content("조회 테스트 게시글")
                .image("img.png")
                .likeCount(0)
                .build();
        em.persist(post);

        mockMvc.perform(get("/api/posts/" + post.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").value("조회 테스트 게시글"))
                .andExpect(jsonPath("$.data.image").value("img.png"))
                .andExpect(jsonPath("$.data.username").value("testuser"));
    }

    @Test
    @DisplayName("좋아요 등록 및 취소 토글 테스트")
    void toggleLikeTest() throws Exception {
        User writer = User.builder().name("작성자").build();
        em.persist(writer);

        User liker = User.builder().name("좋아요누를사람").build();
        em.persist(liker);

        Post post = Post.builder()
                .user(writer)
                .content("좋아요 테스트")
                .likeCount(0)
                .build();
        em.persist(post);

        session.setAttribute("userId", liker.getId());

        mockMvc.perform(post("/api/posts/" + post.getId() + "/like")
                        .session(session))
                .andExpect(status().isOk());

        em.flush(); em.clear();

        Post likedPost = em.find(Post.class, post.getId());
        assertThat(likedPost.getLikeCount()).isEqualTo(1);

        mockMvc.perform(post("/api/posts/" + post.getId() + "/like")
                        .session(session))
                .andExpect(status().isOk());

        em.flush(); em.clear();

        Post unlikedPost = em.find(Post.class, post.getId());
        assertThat(unlikedPost.getLikeCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("존재하지 않는 게시글 좋아요 시도 시 404 오류")
    void toggleLikeNonExist() throws Exception {
        Long nonExistPostId = 9999L;
        User liker = User.builder().name("좋아요누르는사람").build();
        em.persist(liker);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", liker.getId());

        mockMvc.perform(post("/api/posts/" + nonExistPostId + "/like")
                        .session(session))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("존재하지 않는 게시글입니다."));
    }

    @Test
    @DisplayName("본인 게시글 좋아요 누를 시 400 예외")
    void toggleLikeOwnPost_throwBadRequest() throws Exception {
        Post post = Post.builder()
                .user(user)
                .content("본인 게시글")
                .likeCount(0)
                .build();
        em.persist(post);

        mockMvc.perform(post("/api/posts/" + post.getId() + "/like")
                        .session(session))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("본인 게시글에는 좋아요를 누를 수 없습니다."));
    }
}
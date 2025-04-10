package com.newspeed.newspeed.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newspeed.newspeed.domain.post.dto.request.PostRequestDto;

import com.newspeed.newspeed.domain.post.entity.Post;
import com.newspeed.newspeed.domain.post.entity.PostLike;
import com.newspeed.newspeed.domain.post.entity.User;
import com.newspeed.newspeed.domain.post.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager em;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private MockHttpSession session;
    @Autowired
    private ApplicationContext context;
    @BeforeEach
    void setUp() {
        user = User.builder()
                .username("testuser")
                .build();
        em.persist(user);

        session = new MockHttpSession();
        session.setAttribute("userId",19L);
    }
    @Test
    void controllerExistCheck() {
        System.out.println("등록된 컨트롤러: " + context.getBeanNamesForType(PostController.class).length);
        assertThat(context.getBean(PostController.class)).isNotNull();
    }
    @Test
    void sessionCheck() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", 1L);
        assertEquals(1L, session.getAttribute("userId"));
    }

    @Test
    @DisplayName("게시글 생성 통합 테스트")
    void createPost() throws Exception {
        PostRequestDto requestDto = new PostRequestDto();
        requestDto.setContent("테스트 게시글");
        requestDto.setImage("test.png");

        mockMvc.perform(post("/api/posts")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("테스트 게시글"))
                .andExpect(jsonPath("$.image").value("test.png"))
                .andExpect(jsonPath("$.username").value("testuser"));

    }
    @Test
    @DisplayName("뉴스피드 조회 페이징 테스트")
    void getNewsFeed() throws Exception {
        // 게시글 12개 생성
        for (int i = 1; i <= 12; i++) {
            em.persist(com.newspeed.newspeed.domain.post.entity.Post.createPost(user, "내용 " + i, null));
        }

        mockMvc.perform(get("/api/posts?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(10)); // 10개만 가져와야 함
    }
    @Test
    @DisplayName("게시글 단건 조회 테스트")
    void getPost() throws Exception {
        // given
        Post savedPost = Post.createPost(user, "조회 테스트 게시글", "img.png");
        em.persist(savedPost);

        // when & then
        mockMvc.perform(get("/api/posts/" + savedPost.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("조회 테스트 게시글"))
                .andExpect(jsonPath("$.image").value("img.png"))
                .andExpect(jsonPath("$.username").value("testuser"));
    }
    @Test
    @DisplayName("게시글 수정 테스트")
    void updatePost() throws Exception {
        // given
        Post savedPost = Post.createPost(user, "원래 내용", "old.png");
        em.persist(savedPost);

        PostRequestDto updateDto = new PostRequestDto();
        updateDto.setContent("수정된 내용");
        updateDto.setImage("new.png");

        // when & then
        mockMvc.perform(put("/api/posts/" + savedPost.getId())
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("수정된 내용"))
                .andExpect(jsonPath("$.image").value("new.png"));
    }
    @Test
    @DisplayName("게시글 삭제 테스트")
    void deletePost() throws Exception {
        // given
        Post savedPost = Post.createPost(user, "삭제 대상", "img.png");
        em.persist(savedPost);
        Long postId = savedPost.getId();

        // when & then
        mockMvc.perform(delete("/api/posts/" + postId)
                        .session(session))
                .andExpect(status().isNoContent());

        // 삭제됐는지 확인
        assertThat(em.find(Post.class, postId)).isNull();
    }
    @Test
    @DisplayName("좋아요 등록 및 취소 토글 테스트")
    void toggleLikeTest() throws Exception {
        User writer = User.builder().username("작성자").build();
        em.persist(writer);

        User liker = User.builder().username("좋아요누를사람").build();
        em.persist(liker);

        Post post = Post.createPost(writer, "좋아요 테스트", null);
        em.persist(post);
        em.flush();
        session.setAttribute("userId", liker.getId()); // 작성자와 다른 유저

        // 1. 좋아요 누르기
        mockMvc.perform(post("/api/posts/" + post.getId() + "/like")
                        .session(session))
                .andExpect(status().isOk());

        em.flush();
        em.clear();

        // 2. 좋아요 수 증가 확인
        Post likedPost = em.find(Post.class, post.getId());
        assertThat(likedPost.getLikeCount()).isEqualTo(1);

        // 3. 좋아요 다시 눌러서 취소
        mockMvc.perform(post("/api/posts/" + post.getId() + "/like")
                        .session(session))
                .andExpect(status().isOk());

        em.flush();
        em.clear();

        // 4. 좋아요 수 감소 확인
        Post unlikedPost = em.find(Post.class, post.getId());
        assertThat(unlikedPost.getLikeCount()).isEqualTo(0);
    }
    @Test
    @DisplayName("존재하지 않는 게시글  좋아요 시도 시 404 오류")
    void toggleLikeNonExist() throws Exception {
        // given 존재하지 않는 아이디
        Long nonExistPostId = 9999L;
        User liker = User.builder().username("좋아요누르는사람").build();
        em.persist(liker);
        em.flush();

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", liker.getId());

        //when
        mockMvc.perform(post("/api/posts/" + nonExistPostId + "/like")
                .session(session))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("존재하지 않는 게시글입니다"));
    }

    @Test
    @DisplayName("본인 게시글 좋아요 누를 . 400 예외 발생")
    void toggleLikeOwnPost_throwBadRequest() throws Exception {
        //given
        User user = User.builder().username("testuser").build();
        em.persist(user);
        Post post = Post.createPost(user, "testuser", null);
        em.persist(post);
        em.flush();

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", user.getId()); //작성자 본인

        //when
        mockMvc.perform(post("/api/posts/" + post.getId() + "/like")
                .session(session))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("본인 게시글에는 좋아요를 누를 수 없습니다."));
    }
    @Test
    @DisplayName("좋아요 토글 테스트(등록->취소)")
    void toggleLike_addThenRemove() throws Exception {
        User writer = User.builder().username("작성자").build();
        User liker = User.builder().username("좋아요 누르는 사람").build();
        em.persist(writer);
        em.persist(liker);

        Post post = Post.createPost(writer, "testuser", null);
        em.persist(post);
        em.flush();
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", liker.getId());

        //when 좋아요 등록
        mockMvc.perform(post("/api/posts/" + post.getId() + "/like")
                .session(session))
                .andExpect(status().isOk());

        em.flush();
        em.clear();

        Post likePost = em.find(Post.class, post.getId());
        assertThat(likePost.getLikeCount()).isEqualTo(1);

        //when 좋아요 취소
        mockMvc.perform(post("/api/posts/" + post.getId() + "/like")
                .session(session))
                .andExpect(status().isOk());
        em.flush();
        em.clear();
        Post unlikePost = em.find(Post.class, post.getId());
        assertThat(unlikePost.getLikeCount()).isEqualTo(0);

    }
}
package com.newspeed.newspeed.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newspeed.newspeed.domain.post.dto.request.PostRequestDto;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        session.setAttribute("userId", user.getId());
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
}
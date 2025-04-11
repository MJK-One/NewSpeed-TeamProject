package com.newspeed.newspeed.domain.post.controller;

import com.newspeed.newspeed.domain.post.entity.Post;
import com.newspeed.newspeed.domain.post.entity.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class PostNewsFeedTest {
    @Autowired
    EntityManager em;

    private User user;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        user = User.builder().username("testuser").build();
        em.persist(user);

        List<Post> posts = new ArrayList<>();
        posts.add(Post.createPost(user, "포스트 1", null)); // 좋아요 5
        posts.add(Post.createPost(user, "포스트 2", null)); // 좋아요 10
        posts.add(Post.createPost(user, "포스트 3", null)); // 좋아요 3
        posts.add(Post.createPost(user, "포스트 4", null)); // 좋아요 8
        posts.add(Post.createPost(user, "포스트 5", null)); // 좋아요 7

        LocalDateTime base = LocalDateTime.of(2025, 4, 10, 0, 0);
        posts.get(0).setUpdatedAt(base.minusDays(1)); // 4/9
        posts.get(1).setUpdatedAt(base.minusDays(3)); // 4/7
        posts.get(2).setUpdatedAt(base.minusDays(2)); // 4/8
        posts.get(3).setUpdatedAt(base.minusDays(4)); // 4/6
        posts.get(4).setUpdatedAt(base);              // 4/10

        posts.get(0).incrementLikeCount();
        posts.get(0).incrementLikeCount();
        posts.get(0).incrementLikeCount();
        posts.get(0).incrementLikeCount();
        posts.get(0).incrementLikeCount();

        for (int i = 0; i < 10; i++) posts.get(1).incrementLikeCount();
        for (int i = 0; i < 3; i++) posts.get(2).incrementLikeCount();
        for (int i = 0; i < 8; i++) posts.get(3).incrementLikeCount();
        for (int i = 0; i < 7; i++) posts.get(4).incrementLikeCount();

        posts.forEach(em::persist);
        em.flush();
        em.clear();
    }
    @Test
    @DisplayName("뉴스피드 정렬 테스트")
    void getNewsFeedSortByLikeCount() throws Exception {
        mockMvc.perform(get("/api/posts")
                        .param("sort", "like")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].likeCount").value(10))
                .andExpect(jsonPath("$[1].likeCount").value(8))
                .andExpect(jsonPath("$[2].likeCount").value(7));
    }
    @Test
    @DisplayName("뉴스피드 기간 필터 테스트")
    void getNewsFeedByDateRange() throws Exception {
        mockMvc.perform(get("/api/posts")
                        .param("startDate", "2025-04-07")
                        .param("endDate", "2025-04-08")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].content").exists())
                .andExpect(jsonPath("$[1].content").exists());
    }

}

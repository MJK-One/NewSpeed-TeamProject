package com.newspeed.newspeed.domain.post.controller;

import com.newspeed.newspeed.domain.post.entity.Post;

import com.newspeed.newspeed.domain.users.entity.User;
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
        user = User.builder().name("testuser").build();
        em.persist(user);

        LocalDateTime base = LocalDateTime.of(2025, 4, 10, 0, 0);

        List<Post> posts = List.of(
                createPostWithMeta(user, "포스트 1", 5, base.minusDays(1)),
                createPostWithMeta(user, "포스트 2", 10, base.minusDays(3)),
                createPostWithMeta(user, "포스트 3", 3, base.minusDays(2)),
                createPostWithMeta(user, "포스트 4", 8, base.minusDays(4)),
                createPostWithMeta(user, "포스트 5", 7, base)
        );

        posts.forEach(em::persist);
        em.flush();
        em.clear();
    }

    private Post createPostWithMeta(User user, String content, int likeCount, LocalDateTime updatedAt) {
        Post post = Post.builder()
                .user(user)
                .content(content)
                .image(null)
                .likeCount(likeCount)
                .build();

        // Auditing이 적용되었을 경우, 직접 설정 불가하므로 update 쿼리 활용 필요 (여기선 setter 가정)
        post.forceSetUpdatedAt(updatedAt); // 이 메서드는 테스트용으로 Post 엔티티에 추가해야 함
        return post;
    }
    @Test
    @DisplayName("뉴스피드 정렬 테스트")
    void getNewsFeedSortByLikeCount() throws Exception {
        mockMvc.perform(get("/api/posts")
                        .param("sort", "like")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].likeCount").value(10))
                .andExpect(jsonPath("$.data[1].likeCount").value(8))
                .andExpect(jsonPath("$.data[2].likeCount").value(7));
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
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].content").exists())
                .andExpect(jsonPath("$.data[1].content").exists());
    }


}

package com.newspeed.newspeed.domain.post.entity;

import com.newspeed.newspeed.common.entity.BaseTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@Builder
@Getter
@AllArgsConstructor
@Table(name = "post_like")
public class PostLike extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    //연관관계 편의 메서드를 위해 필요한 세터만 열었음
    public void setPost(Post post) {
        this.post = post;
    }

    public static PostLike of(User user, Post post) {
        return PostLike.builder()
                .user(user)
                .post(post)
                .build();
    }
}

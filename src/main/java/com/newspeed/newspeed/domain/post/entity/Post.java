package com.newspeed.newspeed.domain.post.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "post")
public class Post extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "text", columnDefinition = "TEXT")
    private String content;

    @Column(name = "image")
    private String image;

    @Column(name = "likes")
    private int likeCount;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PostLike> postLikes = new ArrayList<>();

    public void incrementLikeCount() {
        this.likeCount++;
    }
    public void decrementLikeCount() {
        this.likeCount--;
    }
    public void update(String content, String image) {
        this.content = content;
        this.image = image;
    }
    //연관관계 편의 메서드
    public void addPostLike(PostLike postLike) {
        this.postLikes.add(postLike);
        postLike.setPost(this);
    }


    public static Post createPost(User user, String content, String image) {
        return Post.builder()
                .user(user)
                .content(content)
                .image(image)
                .likeCount(0)
                .build();
    }

}

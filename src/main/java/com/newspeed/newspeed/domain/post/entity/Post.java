package com.newspeed.newspeed.domain.post.entity;


import com.newspeed.newspeed.common.entity.BaseTime;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "post")
public class Post extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( nullable = false)
    private User user;
    @Column(name = "text", columnDefinition = "TEXT")
    private String content;


    private String image;

    @Column(name = "likes")
    private int likeCount;


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



    public static Post createPost(User user, String content, String image) {
        return Post.builder()
                .user(user)
                .content(content)
                .image(image)
                .likeCount(0)
                .build();
    }

}

package com.newspeed.newspeed.domain.comments.entity;

import com.newspeed.newspeed.common.entity.BaseTimeEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.newspeed.newspeed.domain.users.entity.User;

import jakarta.persistence.*;


@Entity
@Table(name = "Post")
@Data
@NoArgsConstructor
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "text", columnDefinition = "TEXT")
    private String postText;

    @Column(name = "image", length = 500)
    private String image;

    @Column(name = "likes", columnDefinition = "INT DEFAULT 0")
    private Integer commentLikes;
}


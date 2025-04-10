package com.newspeed.newspeed.domain.comments.entity;

import com.newspeed.newspeed.common.entity.BaseTimeEntity;
import com.newspeed.newspeed.domain.users.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "Comment")
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "text", columnDefinition = "TEXT")
    private String commentText;

    @Column(name = "likes", columnDefinition = "INT DEFAULT 0", nullable = false)
    private Integer commentLikes;

    // 생성자 추가
    public Comment(User user, Post post, String commentText) {
        this.user = user;
        this.post = post;
        this.commentText = commentText;
    }
}

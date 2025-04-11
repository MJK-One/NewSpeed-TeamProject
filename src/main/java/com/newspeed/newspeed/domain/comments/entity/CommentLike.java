package com.newspeed.newspeed.domain.comments.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import com.newspeed.newspeed.domain.users.entity.User;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Entity
@Table(name = "comment_like")
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public CommentLike(User user, Comment comment) {
        this.user = user;
        this.comment = comment;
    }
}


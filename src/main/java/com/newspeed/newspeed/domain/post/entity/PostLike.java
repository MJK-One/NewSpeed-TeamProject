package com.newspeed.newspeed.domain.post.entity;


import com.newspeed.newspeed.common.entity.BaseTimeEntity;
import com.newspeed.newspeed.domain.users.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@Builder
@Getter
@AllArgsConstructor
@Table(name = "post_like")
public class PostLike extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;




}

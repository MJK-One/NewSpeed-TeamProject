package com.newspeed.newspeed.domain.users.entity;

import com.newspeed.newspeed.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name="name", nullable = false, length = 255)
    private String name;

    @Column(name="email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name="password", nullable = false, length = 255)
    private String password;

    @Builder
    public User(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
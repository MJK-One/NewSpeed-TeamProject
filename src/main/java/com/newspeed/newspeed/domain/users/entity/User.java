package com.newspeed.newspeed.domain.users.entity;

import com.newspeed.newspeed.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(nullable = false)
    private boolean deleted = false;

    public boolean isDeleted() {
        return deleted;
    }

    public void markAsDeleted() {
        this.deleted = true;
    }

    @Builder
    public User(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

    //유저 이름 업데이트
    public void updateName(String name) {
        this.name = name;
    }

    //유저 이메일 업데이트
    public void updateEmail(String email) {
        this.email = email;
    }

    //유저 비밀번호 업데이트
    public void updatePassword(String password){
        this.password = password;
    }
}


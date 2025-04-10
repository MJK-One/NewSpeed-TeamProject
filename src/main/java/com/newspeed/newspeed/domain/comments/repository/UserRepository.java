package com.newspeed.newspeed.domain.comments.repository;

import com.newspeed.newspeed.domain.comments.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
}

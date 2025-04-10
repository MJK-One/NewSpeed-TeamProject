package com.newspeed.newspeed.domain.post.repository;

import com.newspeed.newspeed.domain.post.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

}

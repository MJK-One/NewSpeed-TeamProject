package com.newspeed.newspeed.domain.users.repository;

import com.newspeed.newspeed.common.exception.base.NotFoundException;
import com.newspeed.newspeed.common.exception.code.enums.ErrorCode;
import com.newspeed.newspeed.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);

    //아이디로 유저 찾기
    default User findUserByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }
}

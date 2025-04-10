package com.newspeed.newspeed.domain.friends.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    @Query("SELECT COUNT(f) FROM Friend f WHERE (f.followToId = :userId OR f.followFromId = :userId) AND f.status = 'FRIENDS'")
    //TODO: 친구일 때 Status가 뭔지 확인하고 바꾸기
    int countFriendsByUserId(@Param("userId") Long userId);

}



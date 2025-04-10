package com.newspeed.newspeed.domain.friends.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    @Query("SELECT COUNT(f) FROM friend f WHERE (f.followToId = :userId OR f.followFromId = :userId) AND f.status = 'ACCEPTED'")
    int countFriendsByUserId(@Param("userId") Long userId);

    @Query("SELECT status FROM friend f WHERE (f.followToId = :userId AND f.followFromId = :profileId) OR (f.followToId = :profileId AND f.followFromId = :userId)")
    String findStatusByUserIdAndProfileId(@Param("userId") Long userId, @Param("profileId") Long profileId);

}



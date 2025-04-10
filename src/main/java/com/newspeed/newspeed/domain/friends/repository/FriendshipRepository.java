package com.newspeed.newspeed.domain.friends.repository;

import com.newspeed.newspeed.domain.friendships.entity.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    @Query("SELECT COUNT(f) FROM Friendship f WHERE (f.followTo.userId = :userId OR f.followFrom.userId = :userId) AND f.status = 'ACCEPTED'")
    int countFriendsByUserId(@Param("userId") Long userId);

    @Query("SELECT f.status FROM Friendship f WHERE (f.followTo.userId = :userId AND f.followFrom.userId = :profileId) OR (f.followTo. userId= :profileId AND f.followFrom.userId = :userId)")
    String findStatusByUserIdAndProfileId(@Param("userId") Long userId, @Param("profileId") Long profileId);

}



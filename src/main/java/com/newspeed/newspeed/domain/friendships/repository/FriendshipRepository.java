package com.newspeed.newspeed.domain.friendships.repository;

import com.newspeed.newspeed.domain.friendships.entity.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship,Long> {
    @Query("SELECT f FROM Friendship f WHERE f.followFrom.id = :userId AND f.followTo.id = :friendId")
    Optional<Friendship> findByUserAndFriendId(Long userId, Long friendId);
}

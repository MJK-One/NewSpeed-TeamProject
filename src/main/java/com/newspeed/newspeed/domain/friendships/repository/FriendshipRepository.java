package com.newspeed.newspeed.domain.friendships.repository;

import com.newspeed.newspeed.domain.friendships.dto.response.FriendSummary;
import com.newspeed.newspeed.domain.friendships.dto.response.GetFriendShipsResponse;
import com.newspeed.newspeed.domain.friendships.entity.Friendship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship,Long> {
    @Query("SELECT f FROM Friendship f WHERE f.followFrom.userId = :userId AND f.followTo.userId = :friendId")
    Optional<Friendship> findByUserAndFriendId(Long userId, Long friendId);

    @Query("""
                SELECT new com.newspeed.newspeed.domain.friendships.dto.response.FriendSummary(
                    CASE 
                        WHEN f.followFrom.userId = :userId THEN f.followTo.userId
                        ELSE f.followFrom.userId
                    END,
                    CASE 
                        WHEN f.followFrom.userId = :userId THEN f.followTo.name
                        ELSE f.followFrom.name
                    END
                )
                FROM Friendship f
                WHERE f.status = 'ACCEPTED'
                  AND (f.followFrom.userId = :userId OR f.followTo.userId = :userId)
            """)
    Page<FriendSummary> findAcceptedByConditions(Long userId, Pageable pageable);

    @Query("""
                SELECT new com.newspeed.newspeed.domain.friendships.dto.response.FriendSummary(
                    CASE 
                        WHEN f.followFrom.userId = :userId THEN f.followTo.userId
                        ELSE f.followFrom.userId
                    END,
                    CASE 
                        WHEN f.followFrom.userId = :userId THEN f.followTo.name
                        ELSE f.followFrom.name
                    END
                )
                FROM Friendship f
                WHERE f.status = 'PENDING'
                  AND (f.followFrom.userId = :userId OR f.followTo.userId = :userId)
            """)
    Page<FriendSummary> findPendingByConditions(Long userId, Pageable pageable);
}

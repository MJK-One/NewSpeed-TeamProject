package com.newspeed.newspeed.domain.friendships.service;

import com.newspeed.newspeed.domain.friendships.dto.request.SendFriendShipRequest;
import com.newspeed.newspeed.domain.friendships.entity.Friendship;
import com.newspeed.newspeed.domain.friendships.entity.value.Status;
import com.newspeed.newspeed.domain.friendships.repository.FriendshipRepository;
import com.newspeed.newspeed.domain.users.entity.User;
import com.newspeed.newspeed.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FriendShipService {

    private final FriendshipRepository friendShipRepository;
    private final UserRepository userRepository;

    public void sendRequest(SendFriendShipRequest request, Long userId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<User> friend = userRepository.findById(request.targetUserId());

        if (user.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 유저 ID 입니다.");
        }

        if (friend.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 친구 ID 입니다.");
        }

        Optional<Friendship> friendship = friendShipRepository.findByUserAndFriendId(userId, request.targetUserId());
        if(friendship.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 친구 요청입니다. (상태: " +friendship.get().getStatus() +")");
        }

        Friendship newFriendship = Friendship.builder()
                .followFrom(user.get())
                .followTo(friend.get())
                .status(Status.PENDING)
                .build();

        friendShipRepository.save(newFriendship);
    }
}

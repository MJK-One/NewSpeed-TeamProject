package com.newspeed.newspeed.domain.friendships.service;

import com.newspeed.newspeed.domain.friendships.dto.request.HandleFriendShipRequest;
import com.newspeed.newspeed.domain.friendships.dto.request.SendFriendShipRequest;
import com.newspeed.newspeed.domain.friendships.dto.response.FriendSummary;
import com.newspeed.newspeed.domain.friendships.dto.response.GetFriendShipRequestsResponse;
import com.newspeed.newspeed.domain.friendships.dto.response.GetFriendShipsResponse;
import com.newspeed.newspeed.domain.friendships.entity.Friendship;
import com.newspeed.newspeed.domain.friendships.entity.value.Status;
import com.newspeed.newspeed.domain.friendships.repository.FriendshipRepository;
import com.newspeed.newspeed.domain.users.entity.User;
import com.newspeed.newspeed.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        if(userId.equals(request.targetUserId())) {
            throw new IllegalArgumentException("자기 자신에게는 친구 요청을 보낼 수 없습니다.");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저 ID 입니다."));
        User friend = userRepository.findById(request.targetUserId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 친구 ID 입니다."));

        Optional<Friendship> friendship = friendShipRepository.findByUserAndFriendId(userId, request.targetUserId());
        if(friendship.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 친구 요청입니다. (상태: " +friendship.get().getStatus() +")");
        }

        Friendship newFriendship = Friendship.builder()
                .followFrom(user)
                .followTo(friend)
                .status(Status.PENDING)
                .build();

        friendShipRepository.save(newFriendship);
    }

    public void handleRequest(Long userId, Long requestId, HandleFriendShipRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저 ID 입니다."));
        Friendship friendship = friendShipRepository.findById(requestId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 친구 요청 ID 입니다."));

        if(!friendship.getFollowTo().equals(user)) {
            throw new IllegalArgumentException("사용자의 친구 요청이 아닙니다.");
        }

        if(request.status() == Status.PENDING) {
            throw new IllegalArgumentException("변경은 수락, 거절만 가능합니다.");
        }

        friendship.updateStatus(request.status());
    }

    public GetFriendShipsResponse getFriendships(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저 ID 입니다."));
        Page<FriendSummary> page = friendShipRepository.findAcceptedByConditions(userId, pageable);

        return GetFriendShipsResponse.builder()
                .friends(page.getContent())
                .totalPageCount(page.getTotalPages())
                .totalElementCount(page.getTotalElements())
                .build();
    }

    public GetFriendShipRequestsResponse getFriendshipRequests(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저 ID 입니다."));
        Page<FriendSummary> page = friendShipRepository.findPendingByConditions(userId, pageable);

        return GetFriendShipRequestsResponse.builder()
                .friends(page.getContent())
                .totalPageCount(page.getTotalPages())
                .totalElementCount(page.getTotalElements())
                .build();
    }
}

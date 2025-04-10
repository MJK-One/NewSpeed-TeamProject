package com.newspeed.newspeed.domain.friendships.service;

import com.newspeed.newspeed.common.exception.base.CustomException;
import com.newspeed.newspeed.common.exception.base.NotFoundException;
import com.newspeed.newspeed.common.exception.code.enums.ErrorCode;
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
@RequiredArgsConstructor
public class FriendShipService {

    private final FriendshipRepository friendShipRepository;
    private final UserRepository userRepository;

    @Transactional
    public void sendRequest(SendFriendShipRequest request, Long userId) {
        if(userId.equals(request.targetUserId())) {
            throw new CustomException(ErrorCode.NOT_ALLOW_REQUEST_MYSELF);
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        User friend = userRepository.findById(request.targetUserId()).orElseThrow(() -> new NotFoundException(ErrorCode.FRIEND_NOT_FOUND));

        Optional<Friendship> friendship = friendShipRepository.findByUserAndFriendId(userId, request.targetUserId());
        if(friendship.isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_FRIENDSHIP,"PENDING");
        }

        Friendship newFriendship = Friendship.builder()
                .followFrom(user)
                .followTo(friend)
                .status(Status.PENDING)
                .build();

        friendShipRepository.save(newFriendship);
    }

    @Transactional
    public void handleRequest(Long userId, Long requestId, HandleFriendShipRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        Friendship friendship = friendShipRepository.findById(requestId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 친구 요청 ID 입니다."));

        if(!friendship.getFollowTo().equals(user)) {
            throw new CustomException(ErrorCode.NOT_YOUR_REQUEST);
        }

        if(request.status() == Status.PENDING) {
            throw new CustomException(ErrorCode.NOT_ALLOW_HANDLE_PENDING);
        }

        friendship.updateStatus(request.status());
    }

    @Transactional(readOnly = true)
    public GetFriendShipsResponse getFriendships(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        Page<FriendSummary> page = friendShipRepository.findAcceptedByConditions(userId, pageable);

        return GetFriendShipsResponse.builder()
                .friends(page.getContent())
                .totalPageCount(page.getTotalPages())
                .totalElementCount(page.getTotalElements())
                .build();
    }

    @Transactional(readOnly = true)
    public GetFriendShipRequestsResponse getFriendshipRequests(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        Page<FriendSummary> page = friendShipRepository.findPendingByConditions(userId, pageable);

        return GetFriendShipRequestsResponse.builder()
                .friends(page.getContent())
                .totalPageCount(page.getTotalPages())
                .totalElementCount(page.getTotalElements())
                .build();
    }
}

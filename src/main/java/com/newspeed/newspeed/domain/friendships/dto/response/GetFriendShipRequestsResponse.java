package com.newspeed.newspeed.domain.friendships.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record GetFriendShipRequestsResponse (
        List<FriendSummary> friends,
        Integer totalPageCount,
        Long totalElementCount
) {
}

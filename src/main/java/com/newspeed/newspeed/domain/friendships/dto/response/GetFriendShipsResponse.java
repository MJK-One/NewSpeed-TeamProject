package com.newspeed.newspeed.domain.friendships.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record GetFriendShipsResponse (
        List<FriendSummary> friends,
        Integer totalPageCount,
        Long totalElementCount
) {
}

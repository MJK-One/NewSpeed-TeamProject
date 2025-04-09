package com.newspeed.newspeed.domain.friendships.dto.request;

import jakarta.validation.constraints.NotNull;

public record SendFriendShipRequest(@NotNull Long targetUserId) {
}

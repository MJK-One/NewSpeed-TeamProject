package com.newspeed.newspeed.domain.friendships.dto.request;

import com.newspeed.newspeed.domain.friendships.entity.value.Status;
import jakarta.validation.constraints.NotNull;

public record HandleFriendShipRequest(@NotNull Status status) {
}

package com.newspeed.newspeed.domain.friendships.entity;

import com.newspeed.newspeed.common.entity.BaseTimeEntity;
import com.newspeed.newspeed.domain.users.entity.User;
import com.newspeed.newspeed.domain.friendships.entity.value.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Friendship extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follow_to_id")
    private User followTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follow_from_id")
    private User followFrom;

    @Enumerated(EnumType.STRING)
    private Status status;

    public void updateStatus(Status status) {
        this.status = status;
    }
}

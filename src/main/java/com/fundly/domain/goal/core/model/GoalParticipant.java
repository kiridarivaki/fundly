package com.fundly.domain.goal.core.model;

import com.fundly.common.model.BaseEntity;
import com.fundly.domain.goal.core.model.enums.GoalRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "goal_members")
@Getter
@Setter
@NoArgsConstructor
public class GoalParticipant extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id")
    private UUID goalId;

    @Enumerated(EnumType.STRING)
    private GoalRole role;
}
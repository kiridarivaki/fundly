package com.fundly.domain.goal.core.model;

import com.fundly.domain.goal.core.model.enums.GoalRole;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoalParticipant {
    private UUID id;
    private UUID userId;
    private UUID goalId;
    private GoalRole role;
}
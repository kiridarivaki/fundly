package com.fundly.domain.goal.core.port.out;

import com.fundly.domain.goal.core.model.enums.GoalRole;

import java.util.UUID;

public interface GoalParticipantRepository {
    boolean existsByGoalIdAndUserIdAndRole(UUID goalId, UUID userId, GoalRole goalRole);

    boolean existsByGoalIdAndUserId(UUID goalId, UUID userId);
}

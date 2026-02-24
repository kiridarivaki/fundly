package com.fundly.domain.goal.infrastructure.persistence.adapter;

import com.fundly.domain.goal.core.model.enums.GoalRole;
import com.fundly.domain.goal.core.port.out.GoalParticipantRepository;
import com.fundly.domain.goal.infrastructure.persistence.jpa.JpaGoalParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GoalParticipantRepositoryImpl implements GoalParticipantRepository {

    private final JpaGoalParticipantRepository jpaRepository;

    @Override
    public boolean existsByGoalIdAndUserIdAndRole(UUID goalId, UUID userId, GoalRole goalRole) {
        return jpaRepository.existsByGoalIdAndUserIdAndRole(goalId, userId, goalRole);
    }

    @Override
    public boolean existsByGoalIdAndUserId(UUID goalId, UUID userId) {
        return jpaRepository.existsByGoalIdAndUserId(goalId, userId);
    }
}
package com.fundly.domain.goal.infrastructure.persistence.jpa;

import com.fundly.domain.goal.core.model.enums.GoalRole;
import com.fundly.domain.goal.infrastructure.persistence.entity.GoalParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaGoalParticipantRepository extends JpaRepository<GoalParticipantEntity, UUID> {
    boolean existsByGoalIdAndUserIdAndRole(UUID goalId, UUID userId, GoalRole goalRole);

    boolean existsByGoalIdAndUserId(UUID goalId, UUID userId);
}
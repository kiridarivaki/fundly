package com.fundly.domain.goal.infrastructure.persistence.mapper;

import com.fundly.domain.goal.core.model.SavingsGoal;
import com.fundly.domain.goal.infrastructure.persistence.entity.SavingsGoalEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {GoalParticipantEntityToModelMapper.class, GoalActivityEntityToModelMapper.class})
public interface SavingsGoalEntityToMapper {
    SavingsGoal toModel(SavingsGoalEntity entity);

    SavingsGoalEntity toEntity(SavingsGoal model);
}
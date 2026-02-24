package com.fundly.domain.goal.infrastructure.persistence.mapper;

import com.fundly.domain.goal.core.model.GoalParticipant;
import com.fundly.domain.goal.infrastructure.persistence.entity.GoalParticipantEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GoalParticipantEntityToModelMapper {
    GoalParticipant toModel(GoalParticipantEntity entity);

    GoalParticipantEntity toEntity(GoalParticipant model);
}
package com.fundly.domain.goal.infrastructure.persistence.mapper;

import com.fundly.domain.goal.core.model.GoalActivity;
import com.fundly.domain.goal.infrastructure.persistence.entity.GoalActivityEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GoalActivityEntityToModelMapper {

    GoalActivity toModel(GoalActivityEntity entity);

    GoalActivityEntity toEntity(GoalActivity model);
}
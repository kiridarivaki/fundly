package com.fundly.domain.goal.infrastructure.mapper;

import com.fundly.domain.goal.core.model.GoalActivity;
import com.fundly.domain.goal.infrastructure.dto.GoalActivityDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GoalActivityMapper {
    @Mapping(source = "audit.createdAt", target = "timestamp")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.firstName", target = "userFirstName")
    @Mapping(source = "eventType.displayName", target = "eventTypeDisplayName")
    GoalActivityDTO toDto(GoalActivity activityEvent);

    List<GoalActivityDTO> toDtoList(List<GoalActivity> activityHistory);
}

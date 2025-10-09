package com.fundly.domain.goal.infrastructure.mapper;

import com.fundly.domain.goal.adapter.web.dto.CreateSavingsGoalRequest;
import com.fundly.domain.goal.adapter.web.dto.UpdateSavingsGoalRequest;
import com.fundly.domain.goal.core.model.SavingsGoal;
import com.fundly.domain.goal.infrastructure.dto.SavingsGoalDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SavingsGoalMapper {
    SavingsGoal toEntity(SavingsGoalDTO goalDto);

    SavingsGoalDTO toDto(SavingsGoal goal);

    SavingsGoal fromCreateDtoToEntity(CreateSavingsGoalRequest createDto);

    void updateFromDto(UpdateSavingsGoalRequest expenseDto, @MappingTarget SavingsGoal goal);

    List<SavingsGoalDTO> toDtoList(List<SavingsGoal> savingsGoals);
}

package com.fundly.domain.expense.infrastructure.mapper;

import com.fundly.domain.expense.adapter.web.dto.CreateExpenseRequest;
import com.fundly.domain.expense.adapter.web.dto.UpdateExpenseRequest;
import com.fundly.domain.expense.core.model.Expense;
import com.fundly.domain.expense.infrastructure.dto.ExpenseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {
    Expense toEntity(ExpenseDTO expenseDto);

    @Mapping(source = "audit.createdAt", target = "createdAt")
    ExpenseDTO toDto(Expense expense);

    Expense fromCreateDtoToEntity(CreateExpenseRequest createDto);

    void updateFromDto(UpdateExpenseRequest expenseDto, @MappingTarget Expense expense);
}
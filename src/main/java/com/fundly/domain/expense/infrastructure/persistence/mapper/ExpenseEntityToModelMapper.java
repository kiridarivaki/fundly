package com.fundly.domain.expense.infrastructure.persistence.mapper;

import com.fundly.domain.expense.core.model.Expense;
import com.fundly.domain.expense.infrastructure.persistence.entity.ExpenseEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExpenseEntityToModelMapper {
    Expense toModel(ExpenseEntity entity);

    ExpenseEntity toEntity(Expense model);
}


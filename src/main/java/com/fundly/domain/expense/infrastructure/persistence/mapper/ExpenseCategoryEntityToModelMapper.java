package com.fundly.domain.expense.infrastructure.persistence.mapper;

import com.fundly.domain.expense.core.model.ExpenseCategory;
import com.fundly.domain.expense.infrastructure.persistence.entity.ExpenseCategoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExpenseCategoryEntityToModelMapper {
    ExpenseCategory toModel(ExpenseCategoryEntity entity);

    ExpenseCategoryEntity toEntity(ExpenseCategory model);
}
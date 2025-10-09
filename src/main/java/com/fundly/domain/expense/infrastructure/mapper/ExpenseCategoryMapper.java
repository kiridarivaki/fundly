package com.fundly.domain.expense.infrastructure.mapper;

import com.fundly.domain.expense.adapter.web.dto.CategoryWithExpensesResponse;
import com.fundly.domain.expense.adapter.web.dto.CreateExpenseCategoryRequest;
import com.fundly.domain.expense.adapter.web.dto.UpdateExpenseCategoryRequest;
import com.fundly.domain.expense.core.model.ExpenseCategory;
import com.fundly.domain.expense.infrastructure.dto.ExpenseCategoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExpenseCategoryMapper {
    @Mapping(target = "userId", source = "user.id")
    ExpenseCategoryDTO toDto(ExpenseCategory expenseCategory);

    ExpenseCategory toEntity(ExpenseCategoryDTO expenseCategoryDto);

    List<ExpenseCategoryDTO> toDtoList(List<ExpenseCategory> expenseCategories);

    ExpenseCategory fromCreateDtoToEntity(CreateExpenseCategoryRequest createDto);

    void updateFromDto(UpdateExpenseCategoryRequest updateDto, @MappingTarget ExpenseCategory expenseCategory);

    @Mapping(source = "category", target = "expenseCategory")
    @Mapping(source = "expenses", target = "expensesInCategory")
    CategoryWithExpensesResponse toCategoryWithExpensesDto(ExpenseCategory category);

    List<CategoryWithExpensesResponse> toCategoryWithExpensesDtoList(List<ExpenseCategory> categories);
}


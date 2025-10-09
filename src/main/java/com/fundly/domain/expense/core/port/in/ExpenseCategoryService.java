package com.fundly.domain.expense.core.port.in;

import com.fundly.domain.expense.adapter.web.dto.CategoryWithExpensesResponse;
import com.fundly.domain.expense.adapter.web.dto.CreateExpenseCategoryRequest;
import com.fundly.domain.expense.adapter.web.dto.UpdateExpenseCategoryRequest;
import com.fundly.domain.expense.infrastructure.dto.ExpenseCategoryDTO;

import java.util.List;
import java.util.UUID;

public interface ExpenseCategoryService {
    public ExpenseCategoryDTO findById(UUID id);

    public List<ExpenseCategoryDTO> findAllForUser();

    public UUID create(CreateExpenseCategoryRequest expenseCategoryDto);

    public void update(UpdateExpenseCategoryRequest expenseCategoryDto, UUID id);

    public void delete(UUID id, boolean withExpenses);

    public List<CategoryWithExpensesResponse> findCategoriesWithExpensesForUser(UUID id);
}

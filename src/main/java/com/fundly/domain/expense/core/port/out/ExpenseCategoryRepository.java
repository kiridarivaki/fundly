package com.fundly.domain.expense.core.port.out;

import com.fundly.domain.expense.core.model.ExpenseCategory;
import com.fundly.domain.report.infrastructure.dto.CategorySummaryDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExpenseCategoryRepository {
    Optional<ExpenseCategory> findById(UUID id);

    Optional<ExpenseCategory> findByName(String name);

    List<ExpenseCategory> findByUserIdOrUserIdIsNull(UUID userId);

    Optional<ExpenseCategory> findByNameAndUserIsNull(String name);

    List<ExpenseCategory> findAllWithExpensesForUser(UUID userId);

    ExpenseCategory save(ExpenseCategory category);

    List<CategorySummaryDTO> findCategorySummaryByUserIdAndDateRange(
            UUID userId, LocalDateTime startDate, LocalDateTime endDate);
}
package com.fundly.domain.expense.core.port.out;

import com.fundly.domain.expense.core.model.Expense;
import com.fundly.domain.report.infrastructure.dto.ExpensesTrendDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExpenseRepository {
    Expense save(Expense expense);

    List<Expense> saveAll(List<Expense> expenses);

    Optional<Expense> findById(UUID id);

    List<Expense> findAllByCategoryId(UUID categoryId);

    Page<Expense> findAllByUserId(UUID userId, Pageable pageable);

    Page<Expense> findAllByUserIdAndKeyword(UUID userId, String keyword, Pageable pageable);

    BigDecimal calculateTotalAmountSpentForPeriod(UUID userId, LocalDateTime startDate, LocalDateTime endDate);

    List<ExpensesTrendDTO> findTimePeriodTrendByUserIdAndDateRange(UUID userId, LocalDateTime startDate, LocalDateTime endDate);
}
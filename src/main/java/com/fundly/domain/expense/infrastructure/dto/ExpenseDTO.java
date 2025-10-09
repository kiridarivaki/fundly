package com.fundly.domain.expense.infrastructure.dto;

import com.fundly.domain.expense.core.model.ExpenseCategory;
import com.fundly.domain.expense.core.model.enums.FrequencyCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExpenseDTO {
    private UUID id;

    private String name;

    private String description;

    private BigDecimal amount;

    private LocalDate date;

    private FrequencyCategory frequency;

    private boolean futureExpense;

    private ExpenseCategory expenseCategory;

    private LocalDateTime createdAt;
}

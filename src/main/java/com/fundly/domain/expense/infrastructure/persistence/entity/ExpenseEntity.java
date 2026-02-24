package com.fundly.domain.expense.infrastructure.persistence.entity;

import com.fundly.common.model.BaseEntity;
import com.fundly.domain.expense.core.model.enums.FrequencyCategory;
import com.fundly.domain.user.infrastructure.persistence.entity.AppUserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "expense")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ExpenseEntity extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "date")
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "frequency")
    private FrequencyCategory frequency;

    @Column(name = "is_future_expense")
    private boolean futureExpense;

    //region mappings
    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUserEntity user;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private ExpenseCategoryEntity expenseCategory;
    //endregion
}

package com.fundly.domain.expense.infrastructure.persistence.entity;

import com.fundly.common.model.BaseEntity;
import com.fundly.domain.expense.core.model.enums.BudgetType;
import com.fundly.domain.user.infrastructure.persistence.entity.AppUserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "expense_category", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "name"}, name = "expense_category_user_id_name_key")})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ExpenseCategoryEntity extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "icon_identifier")
    private String iconIdentifier;

    @Enumerated(EnumType.STRING)
    @Column(name = "budget_type")
    private BudgetType budgetType;

    //region mappings
    @OneToMany(mappedBy = "expenseCategory",
            cascade = {CascadeType.DETACH, CascadeType.PERSIST,
                    CascadeType.MERGE, CascadeType.REFRESH})
    private List<ExpenseEntity> expenses = new ArrayList<ExpenseEntity>();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private AppUserEntity user;
    //endregion

    //region relationship helpers
    public void addExpense(ExpenseEntity expense) {
        this.expenses.add(expense);
        expense.setExpenseCategory(this);
    }
    //endregion
}


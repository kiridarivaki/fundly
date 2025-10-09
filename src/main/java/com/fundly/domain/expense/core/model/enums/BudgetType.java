package com.fundly.domain.expense.core.model.enums;

import lombok.Getter;

@Getter
public enum BudgetType {
    NEEDS("Needs"),
    WANTS("Wants"),
    SAVINGS_AND_DEBT_REPAYMENT("Savings and debt repayment");

    private final String displayName;

    BudgetType(String displayName) {
        this.displayName = displayName;
    }
}

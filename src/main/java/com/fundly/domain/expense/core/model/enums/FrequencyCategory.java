package com.fundly.domain.expense.core.model.enums;

import lombok.Getter;

@Getter
public enum FrequencyCategory {
    DAILY("Daily"),
    WEEKLY("Weekly"),
    MONTHLY("Monthly"),
    YEARLY("Yearly");

    private final String displayName;

    FrequencyCategory(String displayName) {
        this.displayName = displayName;
    }
}

package com.fundly.domain.goal.core.model.enums;

import lombok.Getter;

@Getter
public enum PriorityCategory {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High");

    private final String displayName;

    PriorityCategory(String displayName) {
        this.displayName = displayName;
    }
}

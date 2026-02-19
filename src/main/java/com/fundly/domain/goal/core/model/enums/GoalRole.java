package com.fundly.domain.goal.core.model.enums;

import lombok.Getter;

@Getter
public enum GoalRole {
    ADMIN("Admin"),
    COLLABORATOR("Collaborator");

    private final String displayName;

    GoalRole(String displayName) {
        this.displayName = displayName;
    }
}

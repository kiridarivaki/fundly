package com.fundly.domain.user.core.model.enums;

import lombok.Getter;

@Getter
public enum EmploymentStatus {
    EMPLOYED("Employed"),
    UNEMPLOYED("Unemployed"),
    STUDENT("Student"),
    RETIRED("Retired");

    private final String displayName;

    EmploymentStatus(String displayName) {
        this.displayName = displayName;
    }
}

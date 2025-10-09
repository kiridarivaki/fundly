package com.fundly.domain.goal.adapter.web.dto;

import com.fundly.domain.goal.core.model.enums.PriorityCategory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateSavingsGoalRequest {
    @NotBlank(message = "Name is required.")
    private String name;

    private String description;

    @NotNull(message = "Priority category is required.")
    @Enumerated(EnumType.STRING)
    private PriorityCategory priority;

    @NotNull(message = "Target amount is required.")
    @DecimalMin(value = "0.00", message = "Target amount must be zero or greater.")
    private BigDecimal targetAmount;

    @NotNull(message = "Current amount is required.")
    @DecimalMin(value = "0.00", message = "Current amount must be zero or greater.")
    private BigDecimal currentAmount = BigDecimal.ZERO;

    private boolean isLifeGoal = false;
}

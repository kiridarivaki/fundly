package com.fundly.domain.expense.adapter.web.dto;

import com.fundly.domain.expense.core.model.enums.BudgetType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateExpenseCategoryRequest {
    @NotBlank(message = "Name is required.")
    private String name;

    private String description;

    @NotBlank(message = "Icon is required.")
    private String iconIdentifier;

    @NotNull(message = "Budget type is required.")
    @Enumerated(EnumType.STRING)
    private BudgetType budgetType;
}

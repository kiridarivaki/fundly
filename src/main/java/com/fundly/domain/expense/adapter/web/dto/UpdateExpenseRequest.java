package com.fundly.domain.expense.adapter.web.dto;

import com.fundly.domain.expense.core.model.enums.FrequencyCategory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateExpenseRequest {
    @NotBlank(message = "Name is required.")
    @Size(max = 255, message = "Name cannot exceed 255 characters.")
    private String name;

    @Size(max = 255, message = "Description cannot exceed 255 characters.")
    private String description;

    @NotNull(message = "Amount is required.")
    @DecimalMin(value = "0.00", message = "Amount must be zero or greater.")
    private BigDecimal amount;

    @NotNull(message = "Date of occurrence is required.")
    private LocalDate date;

    @NotNull(message = "Frequency of occurrence is required.")
    @Enumerated(EnumType.STRING)
    private FrequencyCategory frequency;

    @NotNull(message = "Future expense check is required.")
    private boolean futureExpense;
}
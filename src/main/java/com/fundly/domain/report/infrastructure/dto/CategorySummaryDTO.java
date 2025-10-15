package com.fundly.domain.report.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategorySummaryDTO {
    private String categoryName;

    private BigDecimal totalSpent;

    private BigDecimal averageExpense;

    private Double percentageOfTotal;
}

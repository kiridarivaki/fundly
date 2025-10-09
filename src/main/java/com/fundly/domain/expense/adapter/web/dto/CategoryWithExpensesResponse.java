package com.fundly.domain.expense.adapter.web.dto;

import com.fundly.domain.expense.core.model.Expense;
import com.fundly.domain.expense.core.model.ExpenseCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryWithExpensesResponse {
    private ExpenseCategory expenseCategory;

    private List<Expense> expensesInCategory;
}

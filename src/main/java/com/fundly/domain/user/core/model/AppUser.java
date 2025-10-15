package com.fundly.domain.user.core.model;

import com.fundly.common.enums.Currency;
import com.fundly.common.model.BaseEntity;
import com.fundly.domain.expense.core.model.Expense;
import com.fundly.domain.expense.core.model.ExpenseCategory;
import com.fundly.domain.goal.core.model.GoalActivity;
import com.fundly.domain.loan.core.model.Loan;
import com.fundly.domain.report.core.model.Report;
import com.fundly.domain.user.core.model.enums.EmploymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "app_user")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class AppUser extends BaseEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    private UUID id;

    @Column(name = "email")
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_status")
    private EmploymentStatus employmentStatus;

    @Column(name = "monthly_allowance")
    private BigDecimal monthlyAllowance;

    @Enumerated(EnumType.STRING)
    @Column(name = "local_currency")
    private Currency localCurrency;

    //region mappings
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Expense> expenses = new ArrayList<Expense>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExpenseCategory> expenseCategories = new ArrayList<ExpenseCategory>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Loan> loans = new ArrayList<Loan>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GoalActivity> goalActivities = new ArrayList<GoalActivity>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports = new ArrayList<Report>();
    //endregion

    public void addExpense(Expense expense) {
        this.expenses.add(expense);
        expense.setUser(this);
    }

    public void addExpenseCategory(ExpenseCategory expenseCategory) {
        this.expenseCategories.add(expenseCategory);
        expenseCategory.setUser(this);
    }

    public void addLoan(Loan loan) {
        this.loans.add(loan);
        loan.setUser(this);
    }

    public void addGoalEvent(GoalActivity goalActivity) {
        this.goalActivities.add(goalActivity);
        goalActivity.setUser(this);
    }

    public void addReportJobs(Report report) {
        this.reports.add(report);
        report.setUser(this);
    }
}

package com.fundly.domain.user.infrastructure.persistence.entity;

import com.fundly.common.enums.Currency;
import com.fundly.common.model.BaseEntity;
import com.fundly.domain.expense.infrastructure.persistence.entity.ExpenseCategoryEntity;
import com.fundly.domain.expense.infrastructure.persistence.entity.ExpenseEntity;
import com.fundly.domain.goal.core.model.GoalActivity;
import com.fundly.domain.goal.core.model.SavingsGoal;
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
public class AppUserEntity extends BaseEntity {
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
    private List<ExpenseEntity> expenses = new ArrayList<ExpenseEntity>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExpenseCategoryEntity> expenseCategories = new ArrayList<ExpenseCategoryEntity>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Loan> loans = new ArrayList<Loan>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SavingsGoal> savingsGoals = new ArrayList<SavingsGoal>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GoalActivity> goalActivities = new ArrayList<GoalActivity>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports = new ArrayList<Report>();
    //endregion

    //region relationship helpers
    public void addExpense(ExpenseEntity expense) {
        this.expenses.add(expense);
        expense.setUser(this);
    }

    public void addExpenseCategory(ExpenseCategoryEntity expenseCategory) {
        this.expenseCategories.add(expenseCategory);
        expenseCategory.setUser(this);
    }

    public void addLoan(Loan loan) {
        this.loans.add(loan);
        loan.setUser(this);
    }

    public void addSavingsGoal(SavingsGoal savingsGoal) {
        this.savingsGoals.add(savingsGoal);
        savingsGoal.setUser(this);
    }

    public void addGoalEvent(GoalActivity goalActivity) {
        this.goalActivities.add(goalActivity);
        goalActivity.setUser(this);
    }

    public void addReportJobs(Report report) {
        this.reports.add(report);
        report.setUser(this);
    }
    //endregion
}

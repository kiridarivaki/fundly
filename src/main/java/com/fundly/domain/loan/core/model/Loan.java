package com.fundly.domain.loan.core.model;

import com.fundly.common.model.BaseEntity;
import com.fundly.domain.expense.core.model.enums.FrequencyCategory;
import com.fundly.domain.user.core.model.AppUser;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "loan")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Loan extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "current_balance")
    private BigDecimal currentBalance;

    @Column(name = "interest_rate")
    private BigDecimal interestRate;

    @Column(name = "min_monthly_payment")
    private BigDecimal minMonthlyPayment;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_frequency")
    private FrequencyCategory paymentFrequency;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "maturity_date")
    private LocalDate maturityDate;

    //region mappings
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LoanPayment> payments = new ArrayList<LoanPayment>();
    //endregion

    //region relationship helpers
    public void addPayment(LoanPayment payment) {
        this.payments.add(payment);
        payment.setLoan(this);
    }
    //endregion
}


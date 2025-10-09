package com.fundly.domain.loan.core.model;

import com.fundly.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="loan_payment")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LoanPayment extends BaseEntity {
    @Column(name="amount_paid")
    private BigDecimal amountPaid;

    @Column(name="date_paid")
    private LocalDate datePaid;

    //region mappings
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;
    //endregion
}

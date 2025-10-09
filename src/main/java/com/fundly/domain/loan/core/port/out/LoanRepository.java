package com.fundly.domain.loan.core.port.out;

import com.fundly.domain.loan.core.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LoanRepository extends JpaRepository<Loan, UUID> {
}

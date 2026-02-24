package com.fundly.domain.loan.infrastructure.persistence.jpa;

import com.fundly.domain.loan.infrastructure.persistence.entity.LoanPaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaLoanPaymentRepository extends JpaRepository<LoanPaymentEntity, UUID> {
}


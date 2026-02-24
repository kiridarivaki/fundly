package com.fundly.domain.loan.infrastructure.persistence.mapper;

import com.fundly.domain.loan.core.model.Loan;
import com.fundly.domain.loan.infrastructure.persistence.entity.LoanEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoanEntityToModelMapper {

    Loan toModel(LoanEntity entity);

    LoanEntity toEntity(Loan model);
}

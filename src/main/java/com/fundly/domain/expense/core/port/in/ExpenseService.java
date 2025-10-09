package com.fundly.domain.expense.core.port.in;

import com.fundly.domain.expense.adapter.web.dto.CreateExpenseRequest;
import com.fundly.domain.expense.adapter.web.dto.UpdateExpenseRequest;
import com.fundly.domain.expense.infrastructure.dto.ExpenseDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface ExpenseService {
    ExpenseDTO findById(UUID id);

    Page<ExpenseDTO> findAllPaged(Pageable pageable, Optional<String> searchTerm);

    UUID create(@Valid CreateExpenseRequest createExpenseDto);

    void update(UpdateExpenseRequest updateExpenseDto, UUID id);

    void delete(UUID id);

    BigDecimal calculateTotalAmountForCurrentMonth();
}

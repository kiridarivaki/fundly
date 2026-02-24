package com.fundly.domain.expense.infrastructure.persistence.adapter;

import com.fundly.domain.expense.core.model.Expense;
import com.fundly.domain.expense.core.port.out.ExpenseRepository;
import com.fundly.domain.expense.infrastructure.persistence.entity.ExpenseEntity;
import com.fundly.domain.expense.infrastructure.persistence.jpa.JpaExpenseRepository;
import com.fundly.domain.expense.infrastructure.persistence.mapper.ExpenseEntityToModelMapper;
import com.fundly.domain.report.infrastructure.dto.ExpensesTrendDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ExpenseRepositoryImpl implements ExpenseRepository {

    private final JpaExpenseRepository jpaExpenseRepository;
    private final ExpenseEntityToModelMapper mapper;

    @Override
    public Expense save(Expense domainModel) {
        ExpenseEntity entity = mapper.toEntity(domainModel);
        ExpenseEntity savedEntity = jpaExpenseRepository.save(entity);
        return mapper.toModel(savedEntity);
    }

    @Override
    public List<Expense> saveAll(List<Expense> expenses) {
        List<ExpenseEntity> entities = expenses.stream()
                .map(mapper::toEntity)
                .toList();

        List<ExpenseEntity> savedEntities = jpaExpenseRepository.saveAll(entities);

        return savedEntities.stream()
                .map(mapper::toModel)
                .toList();
    }

    @Override
    public Optional<Expense> findById(UUID id) {
        return jpaExpenseRepository.findById(id)
                .map(mapper::toModel);
    }

    @Override
    public List<Expense> findAllByCategoryId(UUID categoryId) {
        return jpaExpenseRepository.findAllByCategoryId(categoryId).stream()
                .map(mapper::toModel)
                .toList();
    }

    @Override
    public Page<Expense> findAllByUserId(UUID userId, Pageable pageable) {
        return jpaExpenseRepository.findAllByUserId(userId, pageable)
                .map(mapper::toModel);
    }

    @Override
    public Page<Expense> findAllByUserIdAndKeyword(UUID userId, String keyword, Pageable pageable) {
        return jpaExpenseRepository.findAllByUserIdAndKeyword(userId, keyword, pageable)
                .map(mapper::toModel);
    }

    @Override
    public BigDecimal calculateTotalAmountSpentForPeriod(UUID userId, LocalDateTime startDate, LocalDateTime endDate) {
        return jpaExpenseRepository.calculateTotalAmountSpentForPeriod(userId, startDate, endDate);
    }

    @Override
    public List<ExpensesTrendDTO> findTimePeriodTrendByUserIdAndDateRange(UUID userId, LocalDateTime startDate, LocalDateTime endDate) {
        return jpaExpenseRepository.findTimePeriodTrendByUserIdAndDateRange(userId, startDate, endDate);
    }//todo: check
}
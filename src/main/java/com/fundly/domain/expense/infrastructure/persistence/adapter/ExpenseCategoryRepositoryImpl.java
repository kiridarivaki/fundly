package com.fundly.domain.expense.infrastructure.persistence.adapter;

import com.fundly.domain.expense.core.model.ExpenseCategory;
import com.fundly.domain.expense.core.port.out.ExpenseCategoryRepository;
import com.fundly.domain.expense.infrastructure.persistence.entity.ExpenseCategoryEntity;
import com.fundly.domain.expense.infrastructure.persistence.jpa.JpaExpenseCategoryRepository;
import com.fundly.domain.expense.infrastructure.persistence.mapper.ExpenseCategoryEntityToModelMapper;
import com.fundly.domain.report.infrastructure.dto.CategorySummaryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ExpenseCategoryRepositoryImpl implements ExpenseCategoryRepository {

    private final JpaExpenseCategoryRepository jpaRepository;
    private final ExpenseCategoryEntityToModelMapper mapper;

    @Override
    public ExpenseCategory save(ExpenseCategory domainModel) {
        ExpenseCategoryEntity entity = mapper.toEntity(domainModel);
        ExpenseCategoryEntity savedEntity = jpaRepository.save(entity);
        return mapper.toModel(savedEntity);
    }

    @Override
    public Optional<ExpenseCategory> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toModel);
    }

    @Override
    public Optional<ExpenseCategory> findByName(String name) {
        return jpaRepository.findByName(name).map(mapper::toModel);
    }

    @Override
    public List<ExpenseCategory> findByUserIdOrUserIdIsNull(UUID userId) {
        return jpaRepository.findByUserIdOrUserIdIsNull(userId).stream()
                .map(mapper::toModel)
                .toList();
    }

    @Override
    public Optional<ExpenseCategory> findByNameAndUserIsNull(String name) {
        return jpaRepository.findByNameAndUserIsNull(name).map(mapper::toModel);
    }

    @Override
    public List<ExpenseCategory> findAllWithExpensesForUser(UUID userId) {
        return jpaRepository.findAllWithExpensesForUser(userId).stream()
                .map(mapper::toModel)
                .toList();
    }

    @Override
    public List<CategorySummaryDTO> findCategorySummaryByUserIdAndDateRange(
            UUID userId, LocalDateTime startDate, LocalDateTime endDate) {
        // Report DTOs are usually passed through directly from the JPA query
        return jpaRepository.findCategorySummaryByUserIdAndDateRange(userId, startDate, endDate);
    }

    public List<ExpenseCategory> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toModel)
                .toList();
    }

    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
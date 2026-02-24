package com.fundly.domain.expense.infrastructure.persistence.jpa;

import com.fundly.domain.expense.infrastructure.persistence.entity.ExpenseEntity;
import com.fundly.domain.report.infrastructure.dto.ExpensesTrendDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaExpenseRepository extends JpaRepository<ExpenseEntity, UUID> {
    @Query("SELECT e FROM ExpenseEntity e WHERE e.id = ?1 AND e.isDeleted = false")
    Optional<ExpenseEntity> findById(UUID id);

    @Query("SELECT e FROM ExpenseEntity e WHERE e.expenseCategory.id =?1 AND e.isDeleted = false")
    List<ExpenseEntity> findAllByCategoryId(UUID categoryId);

    @Query("SELECT e FROM ExpenseEntity e WHERE e.user.id = :userId AND e.isDeleted = false")
    Page<ExpenseEntity> findAllByUserId(UUID userId, Pageable pageable);

    @Query(value = "SELECT * FROM ExpenseEntity e WHERE e.user_id = :userId AND e.is_deleted = false" +
            " AND (:keyword IS NULL OR " +
            "      to_tsvector('english', e.name || ' ' || e.description) @@ to_tsquery('english', :keyword || ':*')" +
            " )",
            nativeQuery = true)
    Page<ExpenseEntity> findAllByUserIdAndKeyword(@Param("userId") UUID userId,
                                                  @Param("keyword") String keyword,
                                                  Pageable pageable);

    @Query("SELECT COALESCE(SUM(e.amount),0) FROM ExpenseEntity e WHERE e.user.id = :userId AND e.audit.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal calculateTotalAmountSpentForPeriod(UUID userId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("""
                SELECT NEW com.example.reports.ExpensesTrendDTO(
                       CONCAT(FUNCTION('YEAR', e.audit.createdAt), '-', FUNCTION('MONTH', e.audit.createdAt)),
                       COALESCE(SUM(e.amount), 0)
                )
                FROM ExpenseEntity e
                WHERE e.user.id = :userId 
                  AND e.audit.createdAt BETWEEN :startDate AND :endDate
                GROUP BY FUNCTION('YEAR', e.audit.createdAt), FUNCTION('MONTH', e.audit.createdAt)
                ORDER BY FUNCTION('YEAR', e.audit.createdAt) ASC, FUNCTION('MONTH', e.audit.createdAt) ASC
            """)
    List<ExpensesTrendDTO> findTimePeriodTrendByUserIdAndDateRange(
            @Param("userId") UUID userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}

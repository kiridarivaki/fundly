package com.fundly.domain.expense.core.port.out;

import com.fundly.domain.expense.core.model.Expense;
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

public interface ExpenseRepository extends JpaRepository<Expense, UUID> {
    @Query("SELECT e FROM Expense e WHERE e.id = ?1 AND e.isDeleted = false")
    Optional<Expense> findById(UUID id);

    @Query("SELECT e FROM Expense e WHERE e.expenseCategory.id =?1 AND e.isDeleted = false")
    List<Expense> findAllByCategoryId(UUID categoryId);

    @Query("SELECT e FROM Expense e WHERE e.user.id = :userId AND e.isDeleted = false")
    Page<Expense> findAllByUserId(UUID userId, Pageable pageable);

    @Query(value = "SELECT * FROM expense e WHERE e.user_id = :userId AND e.is_deleted = false" +
            " AND (:keyword IS NULL OR " +
            "      to_tsvector('english', e.name || ' ' || e.description) @@ to_tsquery('english', :keyword || ':*')" +
            " )",
            nativeQuery = true)
    Page<Expense> findAllByUserIdAndKeyword(@Param("userId") UUID userId,
                                            @Param("keyword") String keyword,
                                            Pageable pageable);

    @Query("SELECT COALESCE(SUM(e.amount),0) FROM Expense e WHERE e.user.id = :userId AND e.audit.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal calculateTotalAmountSpentForPeriod(UUID userId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("""
                SELECT NEW com.example.reports.ExpensesTrendDTO(
                       CONCAT(FUNCTION('YEAR', e.audit.createdAt), '-', FUNCTION('MONTH', e.audit.createdAt)),
                       COALESCE(SUM(e.amount), 0)
                )
                FROM Expense e
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

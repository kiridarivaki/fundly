package com.fundly.domain.expense.core.port.out;

import com.fundly.domain.expense.core.model.ExpenseCategory;
import com.fundly.domain.report.infrastructure.dto.CategorySummaryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, UUID> {
    @Query("SELECT c FROM ExpenseCategory c WHERE c.id = ?1 AND c.isDeleted = false")
    Optional<ExpenseCategory> findById(UUID id);

    @Query("SELECT DISTINCT c FROM ExpenseCategory c JOIN FETCH c.expenses e JOIN e.user u WHERE u.id=?1")
    List<ExpenseCategory> findAllWithExpensesForUser(UUID id);

    List<ExpenseCategory> findByUserIdOrUserIdIsNull(UUID userId);

    Optional<ExpenseCategory> findByNameAndUserIsNull(String name);

    @Query("""
                SELECT NEW com.example.reports.CategorySummaryDTO(
                       e.category, 
                       COALESCE(SUM(e.amount), 0),
                       COALESCE(AVG(e.amount), 0)
                )
                FROM Expense e
                WHERE e.user.id = :userId 
                  AND e.audit.createdAt BETWEEN :startDate AND :endDate
                GROUP BY e.category
                ORDER BY totalSpent DESC
            """)
    List<CategorySummaryDTO> findCategorySummaryByUserIdAndDateRange(
            @Param("userId") UUID userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}

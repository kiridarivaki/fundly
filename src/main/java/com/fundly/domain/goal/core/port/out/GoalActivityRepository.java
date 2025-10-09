package com.fundly.domain.goal.core.port.out;

import com.fundly.domain.goal.core.model.GoalActivity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface GoalActivityRepository extends JpaRepository<GoalActivity, UUID> {
    @Query("SELECT ga FROM GoalActivity ga JOIN FETCH ga.user WHERE ga.goal.id =?1 ORDER BY ga.audit.createdAt DESC")
    List<GoalActivity> findRecentByGoalIdWithUser(UUID goalId, Pageable pageable);
}

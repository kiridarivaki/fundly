package com.fundly.domain.goal.infrastructure.persistence.jpa;

import com.fundly.domain.goal.infrastructure.persistence.entity.GoalActivityEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface JpaGoalActivityRepository extends JpaRepository<GoalActivityEntity, UUID> {
    @Query("SELECT ga FROM GoalActivity ga JOIN FETCH ga.user WHERE ga.goal.id =?1 ORDER BY ga.audit.createdAt DESC")
    List<GoalActivityEntity> findRecentByGoalIdWithUser(UUID goalId, Pageable pageable);
}

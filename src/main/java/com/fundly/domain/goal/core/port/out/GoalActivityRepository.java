package com.fundly.domain.goal.core.port.out;

import com.fundly.domain.goal.core.model.GoalActivity;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface GoalActivityRepository {
    List<GoalActivity> findRecentByGoalId(UUID goalId, Pageable pageable);

    GoalActivity save(GoalActivity goalActivity);
}
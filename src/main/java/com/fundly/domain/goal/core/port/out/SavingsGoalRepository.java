package com.fundly.domain.goal.core.port.out;

import com.fundly.domain.goal.core.model.SavingsGoal;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SavingsGoalRepository {
    Optional<SavingsGoal> findById(UUID id);

    List<SavingsGoal> findAllPersonalGoalsByOwnerId(UUID userId);

    List<SavingsGoal> findAllSharedGoalsByUserId(UUID userId);

    SavingsGoal save(SavingsGoal goal);

    void deleteById(UUID id);
}
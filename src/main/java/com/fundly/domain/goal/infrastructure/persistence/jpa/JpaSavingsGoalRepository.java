package com.fundly.domain.goal.infrastructure.persistence.jpa;

import com.fundly.domain.goal.infrastructure.persistence.entity.SavingsGoalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface JpaSavingsGoalRepository extends JpaRepository<SavingsGoalEntity, UUID> {
    @Query("SELECT g FROM SavingsGoal g WHERE g.ownerId =?1 AND g.participantsIds IS EMPTY")
    List<SavingsGoalEntity> findAllPersonalGoalsByOwnerId(UUID userId);

    @Query("""
             SELECT g FROM SavingsGoal g\s
             WHERE (:userId MEMBER OF g.participantsIds)\s
                OR (g.ownerId =?1 AND g.participantsIds IS NOT EMPTY)
            \s""")
    List<SavingsGoalEntity> findAllSharedGoalsByUserId(UUID userId);
}

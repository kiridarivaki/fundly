package com.fundly.domain.goal.infrastructure.persistence.adapter;

import com.fundly.domain.goal.core.model.GoalActivity;
import com.fundly.domain.goal.core.port.out.GoalActivityRepository;
import com.fundly.domain.goal.infrastructure.persistence.entity.GoalActivityEntity;
import com.fundly.domain.goal.infrastructure.persistence.jpa.JpaGoalActivityRepository;
import com.fundly.domain.goal.infrastructure.persistence.mapper.GoalActivityEntityToModelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GoalActivityRepositoryImpl implements GoalActivityRepository {

    private final JpaGoalActivityRepository jpaRepository;
    private final GoalActivityEntityToModelMapper mapper;

    @Override
    public List<GoalActivity> findRecentByGoalId(UUID goalId, Pageable pageable) {
        List<GoalActivityEntity> entities = jpaRepository.findRecentByGoalIdWithUser(goalId, pageable);

        return entities.stream()
                .map(mapper::toModel)
                .toList();
    }

    @Override
    public GoalActivity save(GoalActivity domainModel) {
        GoalActivityEntity entity = mapper.toEntity(domainModel);
        GoalActivityEntity saved = jpaRepository.save(entity);
        return mapper.toModel(saved);
    }
}
package com.fundly.domain.goal.infrastructure.persistence.adapter;

import com.fundly.domain.goal.core.model.SavingsGoal;
import com.fundly.domain.goal.core.port.out.SavingsGoalRepository;
import com.fundly.domain.goal.infrastructure.persistence.entity.SavingsGoalEntity;
import com.fundly.domain.goal.infrastructure.persistence.jpa.JpaSavingsGoalRepository;
import com.fundly.domain.goal.infrastructure.persistence.mapper.SavingsGoalEntityToMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SavingsGoalRepositoryImpl implements SavingsGoalRepository {

    private final JpaSavingsGoalRepository jpaRepository;
    private final SavingsGoalEntityToMapper mapper;

    @Override
    public Optional<SavingsGoal> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toModel);
    }

    @Override
    public List<SavingsGoal> findAllPersonalGoalsByOwnerId(UUID userId) {
        return jpaRepository.findAllPersonalGoalsByOwnerId(userId).stream()
                .map(mapper::toModel)
                .toList();
    }

    @Override
    public List<SavingsGoal> findAllSharedGoalsByUserId(UUID userId) {
        return jpaRepository.findAllSharedGoalsByUserId(userId).stream()
                .map(mapper::toModel)
                .toList();
    }

    @Override
    public SavingsGoal save(SavingsGoal goal) {
        SavingsGoalEntity entity = mapper.toEntity(goal);
        return mapper.toModel(jpaRepository.save(entity));
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
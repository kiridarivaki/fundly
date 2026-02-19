package com.fundly.domain.goal.core.port.in;

import com.fundly.domain.goal.adapter.web.dto.CreateSavingsGoalRequest;
import com.fundly.domain.goal.adapter.web.dto.UpdateSavingsGoalRequest;
import com.fundly.domain.goal.infrastructure.dto.SavingsGoalDTO;
import com.fundly.domain.user.core.dto.UserDTO;

import java.util.List;
import java.util.UUID;

public interface SavingsGoalService {
    SavingsGoalDTO findById(UUID id);

    List<SavingsGoalDTO> findGoalsForUser(boolean personal);

    UUID create(CreateSavingsGoalRequest createDto);

    void update(UpdateSavingsGoalRequest updateDto, UUID id);

    void delete(UUID id);

    List<UserDTO> findParticipantsForGoal(UUID id);
}

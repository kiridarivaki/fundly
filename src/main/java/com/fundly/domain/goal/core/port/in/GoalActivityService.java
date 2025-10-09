package com.fundly.domain.goal.core.port.in;

import com.fundly.domain.goal.adapter.web.dto.AddContributionRequest;
import com.fundly.domain.goal.adapter.web.dto.AddParticipantRequest;
import com.fundly.domain.goal.infrastructure.dto.GoalActivityDTO;

import java.util.List;
import java.util.UUID;

public interface GoalActivityService {
    void addParticipant(AddParticipantRequest addParticipantDto, UUID id);

    void processContribution(AddContributionRequest contributionDto, UUID id);

    List<GoalActivityDTO> getRecentEventsByGoalId(UUID goalId, int pageNumber);
}

package com.fundly.domain.goal.core.model;

import com.fundly.domain.goal.core.model.enums.PriorityCategory;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavingsGoal {
    private UUID id;
    private String name;
    private String description;
    private PriorityCategory priority;
    private BigDecimal targetAmount;
    private BigDecimal currentAmount;
    private boolean lifeGoal;
    private UUID ownerId;

    private List<GoalParticipant> participants = new ArrayList<>();
    private List<GoalActivity> activities = new ArrayList<>();
}
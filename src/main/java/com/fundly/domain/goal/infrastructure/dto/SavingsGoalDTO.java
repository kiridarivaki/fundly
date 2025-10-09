package com.fundly.domain.goal.infrastructure.dto;

import com.fundly.domain.goal.core.model.enums.PriorityCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SavingsGoalDTO {
    private UUID id;

    private String name;

    private String description;

    private PriorityCategory priority;

    private BigDecimal targetAmount;

    private BigDecimal currentAmount;

    private Boolean isLifeGoal;

    private UUID ownerId;
    
    private List<UUID> participantsIds;
}

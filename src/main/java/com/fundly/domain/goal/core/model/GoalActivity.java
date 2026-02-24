package com.fundly.domain.goal.core.model;

import com.fundly.domain.goal.core.model.enums.EventType;
import com.fundly.domain.user.core.model.AppUser;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GoalActivity {
    private UUID id;
    private EventType eventType;
    private String description;
    private BigDecimal amount;
    private AppUser user;
    private SavingsGoal goal;
}
package com.fundly.domain.goal.core.model.event;

import com.fundly.domain.goal.infrastructure.dto.GoalActivityDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class GoalActivityEvent extends ApplicationEvent {
    private final UUID goalId;
    private final GoalActivityDTO activityDto;

    public GoalActivityEvent(Object source, UUID goalId, GoalActivityDTO activityDto) {
        super(source);
        this.goalId = goalId;
        this.activityDto = activityDto;
    }
}

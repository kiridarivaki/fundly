package com.fundly.domain.goal.core.model.enums;

import lombok.Getter;

@Getter
public enum EventType {
    CONTRIBUTION("A money contribution was made."),
    GOAL_UPDATED("Goal updated."),
    PARTICIPANT_ADDED("New participant!"),
    PARTICIPANT_REMOVED("A participant has left the goal."),
    MILESTONE_ACHIEVED("Milestone Achieved!"),
    GOAL_COMPLETED("Goal completed!");

    private final String displayName;

    EventType(String displayName) {
        this.displayName = displayName;
    }
}

package com.fundly.domain.goal.infrastructure.dto;

import com.fundly.domain.goal.core.model.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GoalActivityDTO {
    private EventType eventType;

    private String eventTypeDisplayName = eventType.getDisplayName();

    private String description;

    private BigDecimal amount;

    private UUID userId;

    private String userFirstName;

    private LocalDateTime timestamp;
}

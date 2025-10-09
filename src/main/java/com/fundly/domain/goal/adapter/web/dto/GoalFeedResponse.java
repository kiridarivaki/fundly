package com.fundly.domain.goal.adapter.web.dto;

import com.fundly.domain.goal.infrastructure.dto.GoalActivityDTO;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GoalFeedResponse {
    private List<GoalActivityDTO> activityHistory;

    private UUID goalId;
    
    private String goalName;

    private BigDecimal amountLeft;
}

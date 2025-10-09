package com.fundly.domain.goal.core.model;

import com.fundly.common.model.BaseEntity;
import com.fundly.domain.goal.core.model.enums.EventType;
import com.fundly.domain.user.core.model.AppUser;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Table(name = "goal_event")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GoalActivity extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private EventType eventType;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "amount")
    private BigDecimal amount;
    //region mappings
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id", nullable = false)
    private SavingsGoal goal;
    //endregion
}

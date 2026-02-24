package com.fundly.domain.goal.infrastructure.persistence.entity;

import com.fundly.common.model.BaseEntity;
import com.fundly.domain.goal.core.model.enums.EventType;
import com.fundly.domain.user.infrastructure.persistence.entity.AppUserEntity;
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
public class GoalActivityEntity extends BaseEntity {
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
    private AppUserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id", nullable = false)
    private SavingsGoalEntity goal;
    //endregion
}

package com.fundly.domain.goal.core.model;

import com.fundly.common.model.BaseEntity;
import com.fundly.domain.goal.core.model.enums.PriorityCategory;
import com.fundly.domain.user.core.model.AppUser;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "savings_goal")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SavingsGoal extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private PriorityCategory priority;

    @Column(name = "target_amount")
    private BigDecimal targetAmount;

    @Column(name = "current_amount", nullable = false)
    private BigDecimal currentAmount = BigDecimal.ZERO;

    @Column(name = "is_life_goal")
    private boolean lifeGoal;

    @Column(name = "user_id", nullable = false)
    private UUID ownerId;

    //region mappings
    @ManyToMany(mappedBy = "userId")
    private List<GoalParticipant> participants = new ArrayList<GoalParticipant>();

    @OneToMany(mappedBy = "goal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GoalActivity> activities = new ArrayList<GoalActivity>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AppUser user;
    //endregion

    //region relationship helpers
    public void addActivity(GoalActivity activity) {
        activities.add(activity);
        activity.setGoal(this);
    }
    //endregion
}


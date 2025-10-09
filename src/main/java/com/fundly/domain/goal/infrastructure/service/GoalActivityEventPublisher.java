package com.fundly.domain.goal.infrastructure.service;

import com.fundly.domain.goal.core.model.event.GoalActivityEvent;
import com.fundly.domain.goal.infrastructure.dto.GoalActivityDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Log4j2
public class GoalActivityEventPublisher {
    private final SimpMessagingTemplate messagingTemplate;

    public GoalActivityEventPublisher(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleGoalActivityEvent(GoalActivityEvent event) {
        GoalActivityDTO eventPayload = event.getActivityDto();
        UUID goalId = event.getGoalId();

        String destination = String.format("/topic/goal/%s/feed", goalId.toString());

        try {
            messagingTemplate.convertAndSend(destination, eventPayload);

            log.info("Successfully broadcasted event for goal with id {} to destination {}.", goalId, destination);
        } catch (Exception ex) {
            log.error("Failed to deliver websocket message to destination {}. With exception: {}", destination, ex.getMessage());
            throw ex;
        }
    }
}

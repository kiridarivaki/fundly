package com.fundly.domain.goal.infrastructure.service;

import com.fundly.common.exception.AlreadyParticipatingException;
import com.fundly.common.exception.SavingsGoalNotFoundException;
import com.fundly.domain.auth.core.port.in.SecurityUserService;
import com.fundly.domain.goal.adapter.web.dto.AddContributionRequest;
import com.fundly.domain.goal.adapter.web.dto.AddParticipantRequest;
import com.fundly.domain.goal.core.model.GoalActivity;
import com.fundly.domain.goal.core.model.SavingsGoal;
import com.fundly.domain.goal.core.model.enums.EventType;
import com.fundly.domain.goal.core.model.event.GoalActivityEvent;
import com.fundly.domain.goal.core.port.in.GoalActivityService;
import com.fundly.domain.goal.core.port.in.SavingsGoalService;
import com.fundly.domain.goal.core.port.out.GoalActivityRepository;
import com.fundly.domain.goal.core.port.out.SavingsGoalRepository;
import com.fundly.domain.goal.infrastructure.dto.GoalActivityDTO;
import com.fundly.domain.goal.infrastructure.mapper.GoalActivityMapper;
import com.fundly.domain.user.core.port.in.UserService;
import com.fundly.domain.user.infrastructure.dto.UserDTO;
import com.fundly.domain.user.infrastructure.mapper.UserDtoToModelMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Log4j2
public class GoalActivityServiceImpl implements GoalActivityService {
    private final GoalActivityRepository activityRepository;
    private final SavingsGoalRepository savingsGoalRepository;
    private final SavingsGoalService savingsGoalService;
    private final UserService userService;
    private final SecurityUserService securityUserService;
    private final GoalActivityMapper activityMapper;
    private final UserDtoToModelMapper userDtoToModelMapper;
    private final ApplicationEventPublisher eventPublisher;

    public GoalActivityServiceImpl(
            GoalActivityRepository activityRepository,
            SavingsGoalRepository savingsGoalRepository,
            SavingsGoalService savingsGoalService,
            UserService userService,
            SecurityUserService securityUserService,
            GoalActivityMapper activityMapper,
            UserDtoToModelMapper userDtoToModelMapper,
            ApplicationEventPublisher eventPublisher) {
        this.activityRepository = activityRepository;
        this.savingsGoalRepository = savingsGoalRepository;
        this.savingsGoalService = savingsGoalService;
        this.userService = userService;
        this.securityUserService = securityUserService;
        this.activityMapper = activityMapper;
        this.userDtoToModelMapper = userDtoToModelMapper;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    @Override
    public void addParticipant(AddParticipantRequest addParticipantDto, UUID id) {
        try {
            UserDTO participantDto = userService.findByEmail(addParticipantDto.getEmail());

            SavingsGoal savedGoal = savingsGoalRepository.findById(id)
                    .orElseThrow(() -> new SavingsGoalNotFoundException("Savings goal with id " + id + " not found."));

            if (savedGoal.getOwnerId().equals(participantDto.getId()))
                throw new AlreadyParticipatingException("User " + participantDto.getEmail() + " is the owner of this goal.");

            List<UserDTO> savedParticipants = savingsGoalService.findParticipantsForGoal(id);

            if (savedParticipants.contains(participantDto))
                throw new AlreadyParticipatingException("User " + participantDto.getEmail() + " already exists in the participants list.");

//            savedGoal.getParticipantsIds().add(participantDto.getId());
//todo: update to use entity
            savingsGoalRepository.save(savedGoal);

            log.info("Successfully added participant {} to goal {}.", participantDto.getId(), id);

            UserDTO userDto = securityUserService.getLoggedInUserInfo();

            String descriptionMessage = String.format("%s was added to the goal.", participantDto.getFirstName());

            GoalActivity goalActivity = GoalActivity.builder()
                    .goal(savedGoal)
                    .user(userDtoToModelMapper.toEntity(userDto))
                    .eventType(EventType.PARTICIPANT_ADDED)
                    .description(descriptionMessage)
                    .build();

            GoalActivity savedGoalActivity = activityRepository.save(goalActivity);

            GoalActivityDTO activityDto = activityMapper.toDto(savedGoalActivity);

            eventPublisher.publishEvent(new GoalActivityEvent(this, savedGoal.getId(), activityDto));

            log.info("Successfully informed other participants of the addition of participant {}.", participantDto.getEmail());
        } catch (Exception ex) {
            log.warn("Failed to add participant {} to goal {}. With exception: {}", addParticipantDto.getEmail(), id, ex.getMessage());
            throw ex;
        }
    }

    @Transactional
    @Override
    public void processContribution(AddContributionRequest contributionDto, UUID id) {
        try {
            SavingsGoal savedGoal = savingsGoalRepository.findById(id)
                    .orElseThrow(() -> new SavingsGoalNotFoundException("Savings goal with id " + id + " not found."));

            UserDTO userDto = securityUserService.getLoggedInUserInfo();

//            if (!userDto.getId().equals(savedGoal.getOwnerId()) && !savedGoal.getParticipantsIds().contains(userDto.getId())) {
//                log.warn("Failed attempt of user {} to contribute to savings goal {}.", userDto.getId(), id);
//                throw new ForbiddenOperationException("You do not have permission to modify this resource.");
//            }
//todo: update to use entity
            savedGoal.setCurrentAmount(savedGoal.getCurrentAmount().add(contributionDto.getAmount()));

            savingsGoalRepository.save(savedGoal);

            log.info("Successfully added contribution to goal {}.", id);

            GoalActivity goalActivity = GoalActivity.builder()
                    .goal(savedGoal)
                    .user(userDtoToModelMapper.toEntity(userDto))
                    .eventType(EventType.CONTRIBUTION)
                    .amount(contributionDto.getAmount())
                    .description(contributionDto.getDescription())
                    .build();

            GoalActivity savedGoalActivity = activityRepository.save(goalActivity);

            GoalActivityDTO activityDto = activityMapper.toDto(savedGoalActivity);

            eventPublisher.publishEvent(new GoalActivityEvent(this, savedGoal.getId(), activityDto));

            log.info("Successfully informed other participants of the contribution to goal {}.", id);
        } catch (Exception ex) {
            log.warn("Failed to add contribution to goal {}. With exception: {}", id, ex.getMessage());
            throw ex;
        }
    }

    @Override
    public List<GoalActivityDTO> getRecentEventsByGoalId(UUID id, int pageNumber) {
        try {
            savingsGoalRepository.findById(id)
                    .orElseThrow(() -> new SavingsGoalNotFoundException("Savings goal with id " + id + " not found."));

            Pageable pageRequest = PageRequest.of(pageNumber, 5);

            List<GoalActivity> activityHistory = activityRepository.findRecentByGoalId(id, pageRequest);

            List<GoalActivityDTO> activityHistoryDto = activityMapper.toDtoList(activityHistory);

            log.info("Successfully fetched {} activity events for goal {}.", activityHistoryDto.size(), id);

            return activityHistoryDto;
        } catch (Exception ex) {
            log.warn("Failed to fetch activity history for goal {}. With exception: {}", id, ex.getMessage());
            throw ex;
        }
    }
}

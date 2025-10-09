package com.fundly.domain.goal.infrastructure.service;

import com.fundly.common.exception.ForbiddenOperationException;
import com.fundly.common.exception.SavingsGoalNotFoundException;
import com.fundly.domain.auth.core.port.in.SecurityUserService;
import com.fundly.domain.goal.adapter.web.dto.CreateSavingsGoalRequest;
import com.fundly.domain.goal.adapter.web.dto.UpdateSavingsGoalRequest;
import com.fundly.domain.goal.core.model.SavingsGoal;
import com.fundly.domain.goal.core.port.in.SavingsGoalService;
import com.fundly.domain.goal.core.port.out.GoalActivityRepository;
import com.fundly.domain.goal.core.port.out.SavingsGoalRepository;
import com.fundly.domain.goal.infrastructure.dto.SavingsGoalDTO;
import com.fundly.domain.goal.infrastructure.mapper.GoalActivityMapper;
import com.fundly.domain.goal.infrastructure.mapper.SavingsGoalMapper;
import com.fundly.domain.user.core.model.AppUser;
import com.fundly.domain.user.core.port.in.UserService;
import com.fundly.domain.user.core.port.out.UserRepository;
import com.fundly.domain.user.infrastructure.dto.UserDTO;
import com.fundly.domain.user.infrastructure.mapper.UserMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Log4j2
public class SavingsGoalServiceImpl implements SavingsGoalService {
    private final SavingsGoalRepository savingsGoalRepository;
    private final SavingsGoalMapper savingsGoalMapper;
    private final SecurityUserService securityUserService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public SavingsGoalServiceImpl(
            SavingsGoalRepository savingsGoalRepository,
            GoalActivityRepository goalActivityRepository,
            SavingsGoalMapper savingsGoalMapper,
            GoalActivityMapper goalActivityMapper,
            SecurityUserService securityUserService,
            UserService userService,
            UserRepository userRepository,
            UserMapper userMapper,
            ApplicationEventPublisher eventPublisher) {
        this.savingsGoalRepository = savingsGoalRepository;
        this.savingsGoalMapper = savingsGoalMapper;
        this.securityUserService = securityUserService;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public SavingsGoalDTO findById(UUID id) {
        try {
            SavingsGoal savedGoal = savingsGoalRepository.findById(id)
                    .orElseThrow(() -> new SavingsGoalNotFoundException("Savings goal with id " + id + " not found."));

            UserDTO userDto = securityUserService.getLoggedInUserInfo();

            if (!userDto.getId().equals(savedGoal.getOwnerId()) && !savedGoal.getParticipantsIds().contains(userDto.getId())) {
                log.warn("Failed attempt of user {} to view savings goal {}.", userDto.getId(), id);
                throw new ForbiddenOperationException("You do not have permission to view this resource.");
            }

            SavingsGoalDTO savingsGoalDto = savingsGoalMapper.toDto(savedGoal);

            log.info("Successfully fetched savings goal with id {}.", savedGoal.getId());

            return savingsGoalDto;
        } catch (Exception ex) {
            log.warn("Failed to view savings goal {}. With exception: {}", id, ex.getMessage());
            throw ex;
        }
    }

    @Override
    public List<SavingsGoalDTO> findGoalsForUser(boolean personal) {
        final String goalType = personal ? "personal" : "shared";

        try {
            UserDTO userDto = securityUserService.getLoggedInUserInfo();

            List<SavingsGoal> goals;

            if (personal) {
                goals = savingsGoalRepository.findAllPersonalGoalsByOwnerId(userDto.getId());
            } else {
                goals = savingsGoalRepository.findAllSharedGoalsByUserId(userDto.getId());
            }

            List<SavingsGoalDTO> goalsDto = savingsGoalMapper.toDtoList(goals);

            log.info("Successfully fetched {} {} goals for user {}.", goals.size(), goalType, userDto.getId());

            return goalsDto;
        } catch (Exception ex) {
            log.warn("Failed to fetch {} goals for logged in user. With exception: {}", goalType, ex.getMessage());
            throw ex;
        }
    }

    @Transactional
    @Override
    public UUID create(CreateSavingsGoalRequest createDto) {
        try {
            SavingsGoal savingsGoal = savingsGoalMapper.fromCreateDtoToEntity(createDto);

            UserDTO userDto = securityUserService.getLoggedInUserInfo();

            savingsGoal.setOwnerId(userDto.getId());

            SavingsGoal savedGoal = savingsGoalRepository.save(savingsGoal);

            log.info("Successfully created savings goal with id {}.", savedGoal.getId());

            return savedGoal.getId();
        } catch (Exception ex) {
            log.warn("Failed to create savings goal {}. With exception: {}", createDto.getName(), ex.getMessage());
            throw ex;
        }
    }

    @Transactional
    @Override
    public void update(UpdateSavingsGoalRequest updateDto, UUID id) {
        try {
            SavingsGoal savedGoal = savingsGoalRepository.findById(id)
                    .orElseThrow(() -> new SavingsGoalNotFoundException("Savings goal with id " + id + " not found."));

            UserDTO userDto = securityUserService.getLoggedInUserInfo();

            if (!userDto.getId().equals(savedGoal.getOwnerId())) {
                log.warn("Failed attempt of user {} to update savings goal {}.", userDto.getId(), id);
                throw new ForbiddenOperationException("You do not have permission to edit this resource.");
            }

            savingsGoalMapper.updateFromDto(updateDto, savedGoal);

            savingsGoalRepository.save(savedGoal);

            log.info("Successfully updated savings goal with id {}.", savedGoal.getId());
        } catch (Exception ex) {
            log.warn("Failed to update savings goal {}. With exception: {}", id, ex.getMessage());
            throw ex;
        }
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        try {
            SavingsGoal savedGoal = savingsGoalRepository.findById(id)
                    .orElseThrow(() -> new SavingsGoalNotFoundException("Savings goal with id " + id + " not found."));

            UserDTO userDto = securityUserService.getLoggedInUserInfo();

            if (!userDto.getId().equals(savedGoal.getOwnerId())) {
                log.warn("Failed attempt of user {} to delete savings goal {}.", userDto.getId(), id);
                throw new ForbiddenOperationException("You do not have permission to delete this resource.");
            }

            savedGoal.setDeleted(true);

            savingsGoalRepository.save(savedGoal);

            log.info("Successfully deleted savings goal with id {}.", savedGoal.getId());
        } catch (Exception ex) {
            log.warn("Failed to delete savings goal {}. With exception: {}", id, ex.getMessage());
            throw ex;
        }
    }

    @Override
    public List<UserDTO> findParticipantsForGoal(UUID id) {
        try {
            SavingsGoal savedGoal = savingsGoalRepository.findById(id)
                    .orElseThrow(() -> new SavingsGoalNotFoundException("Savings goal with id " + id + " not found"));

            List<UUID> participantIds = new ArrayList<>(savedGoal.getParticipantsIds());
            participantIds.add(savedGoal.getOwnerId());

            List<AppUser> participants = userRepository.findAllByIdIn(participantIds);

            List<UserDTO> participantsDto = userMapper.toDtoList(participants);

            log.info("Successfully fetched {} participants for goal {}.", participantsDto.size(), id);

            return participantsDto;
        } catch (Exception ex) {
            log.warn("Failed to fetch participants for goal {}. With exception: {}", id, ex.getMessage());
            throw ex;
        }
    }

}

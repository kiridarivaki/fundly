package com.fundly.domain.user.infrastructure.service;

import com.fundly.common.exception.ForbiddenOperationException;
import com.fundly.common.exception.UserAlreadyExistsException;
import com.fundly.common.exception.UserNotFoundException;
import com.fundly.domain.auth.core.port.in.SecurityUserService;
import com.fundly.domain.expense.core.port.in.ExpenseCategoryService;
import com.fundly.domain.user.core.model.AppUser;
import com.fundly.domain.user.core.port.in.UserService;
import com.fundly.domain.user.core.port.out.UserRepository;
import com.fundly.domain.user.adapter.web.dto.RegisterRequest;
import com.fundly.domain.user.infrastructure.dto.UserDTO;
import com.fundly.domain.user.infrastructure.mapper.UserMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Log4j2
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final SecurityUserService securityUserService;
    private final ExpenseCategoryService categoryService;
    private final UserMapper mapper;

    public UserServiceImpl(
            UserRepository userRepository,
            SecurityUserService securityUserService,
            ExpenseCategoryService categoryService,
            UserMapper mapper) {
        this.userRepository = userRepository;
        this.securityUserService = securityUserService;
        this.categoryService = categoryService;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public void register(RegisterRequest registerDto) {
        try {
            userRepository.findByEmail(registerDto.getEmail())
                    .ifPresent(existingUser -> {
                        throw new UserAlreadyExistsException(registerDto.getEmail());
                    });

            AppUser user = mapper.registerDtoToEntity(registerDto);
            userRepository.save(user);

            log.info("User information of user with email {} saved.", user.getEmail());

            securityUserService.save(registerDto);

            log.info("User with email {} registered successfully.", user.getEmail());
        } catch (Exception ex) {
            log.warn("Failed to register user {}. With exception: {}", registerDto.getEmail(), ex.getMessage());
            throw ex;
        }
    }

    @Override
    public UserDTO findById(UUID id) {
        try {
            AppUser savedUser = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found."));

            UserDTO userDto = mapper.toDto(savedUser);

            log.info("Successfully retrieved user with id {}.", id);

            return userDto;
        } catch (Exception ex) {
            log.warn("Failed to find user {}. With exception: {}", id, ex.getMessage());
            throw ex;
        }
    }

    @Override
    public UserDTO findByEmail(String email) {
        try {
            AppUser savedUser = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found."));

            UserDTO userDto = mapper.toDto(savedUser);

            log.info("Successfully retrieved user with email {}.", email);

            return userDto;
        } catch (Exception ex) {
            log.warn("Failed to find user with email {}. With exception: {}", email, ex.getMessage());
            throw ex;
        }
    }

    @Transactional
    @Override
    public void update(UserDTO updateDto, UUID id) {
        try {
            AppUser savedUser = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found."));

            UserDTO userDto = securityUserService.getLoggedInUserInfo();

            if (userDto.getId() != savedUser.getId())
                throw new ForbiddenOperationException("You do not have permission to update the user " + savedUser.getId());

            mapper.updateFromDto(updateDto, savedUser);

            userRepository.save(savedUser);

            log.info("Successfully updated user with id {}.", savedUser.getId());
        } catch (Exception ex) {
            log.warn("Failed to update user {}. With exception: {}", id, ex.getMessage());
            throw ex;
        }
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        try {
            AppUser savedUser = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found."));

            UserDTO userDto = securityUserService.getLoggedInUserInfo();

            if (userDto.getId() != savedUser.getId())
                throw new ForbiddenOperationException("You do not have permission to update the expense " + savedUser.getId());

            savedUser.setDeleted(true);
            userRepository.save(savedUser);

            log.info("Successfully deleted user with id {}.", savedUser.getId());
        } catch (Exception ex) {
            log.warn("Failed to delete user {}. With exception: {}", id, ex.getMessage());
            throw ex;
        }
    }
}

package com.fundly.domain.auth.infrastructure.service;

import com.fundly.common.enums.Role;
import com.fundly.domain.auth.core.model.CustomUserDetails;
import com.fundly.domain.auth.core.model.SecurityUser;
import com.fundly.domain.auth.core.port.in.SecurityUserService;
import com.fundly.domain.auth.core.port.out.SecurityUserRepository;
import com.fundly.domain.auth.infrastructure.mapper.SecurityUserMapper;
import com.fundly.domain.user.infrastructure.dto.UserDTO;
import com.fundly.domain.user.infrastructure.mapper.UserDtoToModelMapper;
import com.fundly.domain.user.ui.dto.RegisterRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
public class SecurityUserServiceImpl implements SecurityUserService {
    private final SecurityUserRepository securityUserRepository;
    private final SecurityUserMapper securityUserMapper;
    private final UserDtoToModelMapper userDtoToModelMapper;
    private final PasswordEncoder passwordEncoder;

    public SecurityUserServiceImpl(
            SecurityUserRepository securityUserRepository,
            SecurityUserMapper securityUserMapper,
            UserDtoToModelMapper userDtoToModelMapper,
            PasswordEncoder passwordEncoder) {
        this.securityUserRepository = securityUserRepository;
        this.securityUserMapper = securityUserMapper;
        this.userDtoToModelMapper = userDtoToModelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO getLoggedInUserInfo() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new IllegalStateException("User is not authenticated.");
            }

            Object principal = authentication.getPrincipal();
            if (!(principal instanceof CustomUserDetails userDetails)) {
                throw new IllegalStateException("Invalid principal type.");
            }

            UserDTO userDto = userDtoToModelMapper.toDto(userDetails.getAppUser());

            log.info("Logged in user's {} info loaded successfully.", userDto.getEmail());

            return userDto;
        } catch (Exception ex) {
            log.warn("Failed to retrieve logged in user. With exception: {}", ex.getMessage());
            throw ex;
        }
    }

    @Transactional
    @Override
    public void save(RegisterRequest registerDto) {
        try {
            SecurityUser securityUser = new SecurityUser();
            securityUser.addAuthority(Role.USER);

            String encodedPassword = passwordEncoder.encode(registerDto.getPassword());
            registerDto.setPassword(encodedPassword);

            securityUserMapper.updateFromRegisterDto(registerDto, securityUser);

            securityUserRepository.save(securityUser);

            log.info("User with email {} saved successfully.", registerDto.getEmail());
        } catch (Exception ex) {
            log.warn("Failed to save user with email {}. With exception: {}", registerDto.getEmail(), ex.getMessage());
            throw ex;
        }
    }
}

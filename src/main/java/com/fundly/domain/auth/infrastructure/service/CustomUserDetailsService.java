package com.fundly.domain.auth.infrastructure.service;

import com.fundly.common.exception.UserNotFoundException;
import com.fundly.domain.auth.core.model.CustomUserDetails;
import com.fundly.domain.auth.core.model.SecurityUser;
import com.fundly.domain.auth.core.port.out.SecurityUserRepository;
import com.fundly.domain.user.core.model.AppUser;
import com.fundly.domain.user.core.port.out.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class CustomUserDetailsService implements UserDetailsService {
    private final SecurityUserRepository securityUserRepository;
    private final UserRepository userRepository;

    public CustomUserDetailsService(SecurityUserRepository securityUserRepository, UserRepository userRepository) {
        this.securityUserRepository = securityUserRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        SecurityUser securityUser = securityUserRepository.findByUsername(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email " + email + " not found."));

        AppUser appUserEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("AppUser not found for security user " + email));

        log.info("User with email {} loaded successfully.", email);

        return new CustomUserDetails(securityUser, appUserEntity);
    }
}

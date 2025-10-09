package com.fundly.domain.auth.core.port.out;

import com.fundly.domain.auth.core.model.SecurityUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SecurityUserRepository extends JpaRepository<SecurityUser, String> {
    Optional<SecurityUser> findByUsername(String username);
}

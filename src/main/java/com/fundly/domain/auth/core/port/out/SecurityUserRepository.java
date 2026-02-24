package com.fundly.domain.auth.core.port.out;

import com.fundly.domain.auth.core.model.SecurityUser;

import java.util.Optional;

public interface SecurityUserRepository {
    Optional<SecurityUser> findByUsername(String username);

    SecurityUser save(SecurityUser user);
}

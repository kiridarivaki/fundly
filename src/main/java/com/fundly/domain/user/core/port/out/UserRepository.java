package com.fundly.domain.user.core.port.out;

import com.fundly.domain.user.core.model.AppUser;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<AppUser> findByEmail(String email);

    Optional<AppUser> findById(UUID id);

    List<AppUser> findAllByIds(List<UUID> ids);

    AppUser save(AppUser user);
}

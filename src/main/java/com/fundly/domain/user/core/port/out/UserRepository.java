package com.fundly.domain.user.core.port.out;

import com.fundly.domain.user.core.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<AppUser, UUID> {
    @Query("SELECT u FROM AppUser u WHERE u.email=?1 AND u.isDeleted =false")
    Optional<AppUser> findByEmail(String email);

    List<AppUser> findAllByIdIn(List<UUID> userIds);
}

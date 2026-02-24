package com.fundly.domain.user.infrastructure.persistence.jpa;

import com.fundly.domain.user.infrastructure.persistence.entity.AppUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<AppUserEntity, UUID> {
    @Query("SELECT u FROM AppUser u WHERE u.email=?1 AND u.isDeleted =false")
    Optional<AppUserEntity> findByEmail(String email);

    List<AppUserEntity> findAllByIdIn(List<UUID> userIds);
}

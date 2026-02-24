package com.fundly.domain.auth.infrastructure.persistence.jpa;

import com.fundly.domain.auth.infrastructure.persistence.entity.SecurityUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaSecurityUserRepository extends JpaRepository<SecurityUserEntity, UUID> {
    Optional<SecurityUserEntity> findByUsername(String username);
}

package com.fundly.domain.auth.infrastructure.persistence.adapter;

import com.fundly.domain.auth.core.model.SecurityUser;
import com.fundly.domain.auth.core.port.out.SecurityUserRepository;
import com.fundly.domain.auth.infrastructure.persistence.entity.SecurityUserEntity;
import com.fundly.domain.auth.infrastructure.persistence.jpa.JpaSecurityUserRepository;
import com.fundly.domain.auth.infrastructure.persistence.mapper.SecurityUserEntityToModelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityUserRepositoryImpl implements SecurityUserRepository {

    private final JpaSecurityUserRepository jpaRepository;
    private final SecurityUserEntityToModelMapper mapper;

    @Override
    public Optional<SecurityUser> findByUsername(String username) {
        return jpaRepository.findByUsername(username)
                .map(mapper::toModel);
    }

    @Override
    public SecurityUser save(SecurityUser user) {
        SecurityUserEntity entity = mapper.toEntity(user);
        SecurityUserEntity saved = jpaRepository.save(entity);
        return mapper.toModel(saved);
    }
}

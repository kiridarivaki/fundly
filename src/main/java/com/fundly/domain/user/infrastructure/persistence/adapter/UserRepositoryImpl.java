package com.fundly.domain.user.infrastructure.persistence.adapter;

import com.fundly.domain.user.core.model.AppUser;
import com.fundly.domain.user.core.port.out.UserRepository;
import com.fundly.domain.user.infrastructure.persistence.entity.AppUserEntity;
import com.fundly.domain.user.infrastructure.persistence.jpa.JpaUserRepository;
import com.fundly.domain.user.infrastructure.persistence.mapper.UserEntityToModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserRepositoryImpl implements UserRepository {
    private final JpaUserRepository jpaRepository;
    private final UserEntityToModelMapper mapper;

    public UserRepositoryImpl(JpaUserRepository jpaRepository, UserEntityToModelMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<AppUser> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toModel);
    }

    @Override
    public Optional<AppUser> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(mapper::toModel);
    }

    @Override
    public List<AppUser> findAllByIds(List<UUID> ids) {
        return jpaRepository.findAllByIdIn(ids).stream()
                .map(mapper::toModel)
                .toList();
    }

    @Override
    public AppUser save(AppUser user) {
        AppUserEntity entity = mapper.toEntity(user);
        AppUserEntity savedEntity = jpaRepository.save(entity);

        return mapper.toModel(savedEntity);
    }
}
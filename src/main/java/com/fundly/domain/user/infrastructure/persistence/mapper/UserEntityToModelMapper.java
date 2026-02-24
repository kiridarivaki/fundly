package com.fundly.domain.user.infrastructure.persistence.mapper;

import com.fundly.domain.user.core.model.AppUser;
import com.fundly.domain.user.infrastructure.persistence.entity.AppUserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserEntityToModelMapper {
    AppUser toModel(AppUserEntity entity);

    AppUserEntity toEntity(AppUser model);
}

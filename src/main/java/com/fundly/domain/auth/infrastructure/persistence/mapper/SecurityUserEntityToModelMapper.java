package com.fundly.domain.auth.infrastructure.persistence.mapper;

import com.fundly.domain.auth.core.model.SecurityUser;
import com.fundly.domain.auth.infrastructure.persistence.entity.SecurityUserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SecurityUserEntityToModelMapper {
    SecurityUser toModel(SecurityUserEntity entity);

    SecurityUserEntity toEntity(SecurityUser model);
}

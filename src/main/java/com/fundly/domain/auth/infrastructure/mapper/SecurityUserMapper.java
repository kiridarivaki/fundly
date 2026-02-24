package com.fundly.domain.auth.infrastructure.mapper;

import com.fundly.domain.auth.core.model.SecurityUser;
import com.fundly.domain.auth.infrastructure.dto.SecurityUserDTO;
import com.fundly.domain.user.ui.dto.RegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SecurityUserMapper {
    SecurityUserDTO toDto(SecurityUser securityUser);

    @Mapping(source = "email", target = "username")
    void updateFromRegisterDto(RegisterRequest registerDto, @MappingTarget SecurityUser securityUser);
}

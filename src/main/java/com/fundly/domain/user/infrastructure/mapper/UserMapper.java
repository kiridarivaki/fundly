package com.fundly.domain.user.infrastructure.mapper;

import com.fundly.domain.user.adapter.web.dto.RegisterRequest;
import com.fundly.domain.user.core.model.AppUser;
import com.fundly.domain.user.infrastructure.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    AppUser registerDtoToEntity(RegisterRequest registerDto);

    UserDTO toDto(AppUser user);

    AppUser toEntity(UserDTO userDto);

    void updateFromDto(UserDTO dto, @MappingTarget AppUser user);

    List<UserDTO> toDtoList(List<AppUser> users);
}
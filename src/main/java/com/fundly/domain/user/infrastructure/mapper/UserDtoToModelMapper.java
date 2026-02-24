package com.fundly.domain.user.infrastructure.mapper;

import com.fundly.domain.user.core.model.AppUser;
import com.fundly.domain.user.infrastructure.dto.UserDTO;
import com.fundly.domain.user.ui.dto.RegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserDtoToModelMapper {
    AppUser registerDtoToEntity(RegisterRequest registerDto);

    UserDTO toDto(AppUser user);

    AppUser toEntity(UserDTO userDto);

    void updateFromDto(UserDTO dto, @MappingTarget AppUser user);

    List<UserDTO> toDtoList(List<AppUser> users);
}
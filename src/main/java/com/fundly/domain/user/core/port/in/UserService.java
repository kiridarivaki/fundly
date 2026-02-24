package com.fundly.domain.user.core.port.in;


import com.fundly.domain.user.infrastructure.dto.UserDTO;
import com.fundly.domain.user.ui.dto.RegisterRequest;

import java.util.UUID;

public interface UserService {
    void register(RegisterRequest registerDto);

    UserDTO findById(UUID id);

    UserDTO findByEmail(String email);

    void update(UserDTO updateDto, UUID id);

    void delete(UUID id);
}

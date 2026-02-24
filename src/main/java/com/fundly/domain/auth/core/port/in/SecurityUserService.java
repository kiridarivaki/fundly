package com.fundly.domain.auth.core.port.in;


import com.fundly.domain.user.infrastructure.dto.UserDTO;
import com.fundly.domain.user.ui.dto.RegisterRequest;

public interface SecurityUserService {
    UserDTO getLoggedInUserInfo();

    void save(RegisterRequest registerDto);
}

package com.fundly.domain.auth.core.port.in;


import com.fundly.domain.user.adapter.web.dto.RegisterRequest;
import com.fundly.domain.user.infrastructure.dto.UserDTO;

public interface SecurityUserService {
    UserDTO getLoggedInUserInfo();

    void save(RegisterRequest registerDto);
}

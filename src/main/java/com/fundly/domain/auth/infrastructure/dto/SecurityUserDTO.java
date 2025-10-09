package com.fundly.domain.auth.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SecurityUserDTO {
    private String username;

    private boolean isEnabled = true;
}

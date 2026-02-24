package com.fundly.domain.auth.core.model;

import com.fundly.common.enums.Role;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Authority {
    private UUID id;
    private Role authority;

    public Authority(Role role) {
        this.authority = role;
    }
}
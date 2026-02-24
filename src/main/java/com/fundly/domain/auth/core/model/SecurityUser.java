package com.fundly.domain.auth.core.model;

import com.fundly.common.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@ToString
public class SecurityUser {
    private String username;
    private String password;
    private boolean isEnabled = true;
    private List<Authority> authorities = new ArrayList<>();

    public SecurityUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void addAuthority(Role role) {
        Authority authority = new Authority(role);
        this.authorities.add(authority);
    }
}
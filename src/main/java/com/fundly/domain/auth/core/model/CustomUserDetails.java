package com.fundly.domain.auth.core.model;

import com.fundly.domain.user.core.model.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {
    private final SecurityUser securityUser;
    private final AppUser appUser;

    public CustomUserDetails(SecurityUser securityUser, AppUser appUser) {
        this.securityUser = securityUser;
        this.appUser = appUser;
    }

    public SecurityUser getSecurityUser() {
        return securityUser;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return securityUser.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority().name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return securityUser.getPassword();
    }

    @Override
    public String getUsername() {
        return securityUser.getUsername();
    }

    @Override
    public boolean isEnabled() {
        return securityUser.isEnabled();
    }
}

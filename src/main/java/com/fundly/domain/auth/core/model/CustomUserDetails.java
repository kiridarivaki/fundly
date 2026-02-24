package com.fundly.domain.auth.core.model;

import com.fundly.domain.user.core.model.AppUser;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@Getter
public class CustomUserDetails implements UserDetails {
    private final SecurityUser securityUser;
    private final AppUser appUser; // Decoupled from JPA

    public CustomUserDetails(SecurityUser securityUser, AppUser appUser) {
        this.securityUser = securityUser;
        this.appUser = appUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return securityUser.getAuthorities().stream()
                .map(auth -> new SimpleGrantedAuthority(auth.getAuthority().name()))
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

    // Standard UserDetails methods
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
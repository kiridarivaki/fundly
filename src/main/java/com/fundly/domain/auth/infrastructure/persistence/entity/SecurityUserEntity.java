package com.fundly.domain.auth.infrastructure.persistence.entity;

import com.fundly.common.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Setter
@Getter
@ToString
public class SecurityUserEntity {
    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "enabled")
    private boolean isEnabled = true;

    //region mappings
    @OneToMany(mappedBy = "securityUser", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<AuthorityEntity> authorities = new ArrayList<AuthorityEntity>();
    //endregion

    public SecurityUserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }

    //region relationship helpers
    public void addAuthority(Role role) {
        AuthorityEntity authority = new AuthorityEntity(role);
        authority.setSecurityUser(this);
        this.authorities.add(authority);
    }
    //endregion
}
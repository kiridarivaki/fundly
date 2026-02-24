package com.fundly.domain.auth.infrastructure.persistence.entity;

import com.fundly.common.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "authorities")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class AuthorityEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    private UUID id;

    @Column(name = "authority")
    @Enumerated(EnumType.STRING)
    private Role authority;

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private SecurityUserEntity securityUser;

    public AuthorityEntity(Role authority) {
        this.authority = authority;
    }
}



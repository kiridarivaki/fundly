package com.fundly.common.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name="id", updatable = false, nullable = false)
    private UUID id;

    @Column(name="is_deleted")
    private boolean isDeleted = false;

    @Embedded
    private Audit audit;
}

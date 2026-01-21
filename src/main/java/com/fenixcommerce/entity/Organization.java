package com.fenixcommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tenant")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "tenant_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "tenant_name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrgStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}

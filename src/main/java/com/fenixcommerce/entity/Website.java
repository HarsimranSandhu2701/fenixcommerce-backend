package com.fenixcommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "store", uniqueConstraints = {
    @UniqueConstraint(name = "uk_store_code_per_tenant", columnNames = {"tenant_id", "store_code"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Website {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "store_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Organization organization;

    @Column(name = "store_code", nullable = false)
    private String code;

    @Column(name = "store_name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Platform platform;

    @Column
    private String domain;

    @Column
    private String timezone;

    @Column
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WebsiteStatus status;

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

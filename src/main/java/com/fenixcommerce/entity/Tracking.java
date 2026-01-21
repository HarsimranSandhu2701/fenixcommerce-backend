package com.fenixcommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tracking")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tracking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "tracking_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fulfillment_id", nullable = false)
    private Fulfillment fulfillment;

    @Column(name = "tracking_number", nullable = false)
    private String trackingNumber;

    @Column
    private String carrier;

    @Column(name = "tracking_url")
    private String trackingUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrackingStatus status;

    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary = false;

    @Column(name = "last_event_at")
    private Instant lastEventAt;

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

package com.fenixcommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "fulfillments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fulfillment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "fulfillment_id", columnDefinition = "BINARY(16)")
    private UUID fulfillmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column
    private String externalFulfillmentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FulfillmentTrackingStatus fulfillmentStatus;

    @Column
    private String carrier;

    @Column
    private String serviceLevel;

    @Column
    private String shipFromLocation;

    @Column
    private LocalDateTime shippedAt;

    @Column
    private LocalDateTime deliveredAt;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
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

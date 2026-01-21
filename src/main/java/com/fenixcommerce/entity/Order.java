package com.fenixcommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id", columnDefinition = "BINARY(16)")
    private UUID orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Website website;

    @Column(nullable = false)
    private String externalOrderId;

    @Column
    private String externalOrderNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FinancialStatus financialStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FulfillmentStatus fulfillmentStatus;

    @Column
    private String customerEmail;

    @Column(name = "order_total_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal orderTotal;

    @Column
    private String currency;

    @Column(name = "order_created_at")
    private LocalDateTime orderCreatedAt;

    @Column(name = "order_updated_at")
    private LocalDateTime orderUpdatedAt;

    @Column(name = "ingested_at", nullable = false)
    private Instant ingestedAt;
}

package com.fenixcommerce.dto;

import com.fenixcommerce.entity.FulfillmentTrackingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FulfillmentDto {
    private UUID id;
    private UUID orderId;
    private String externalFulfillmentId;
    private FulfillmentTrackingStatus status;
    private String carrier;
    private String serviceLevel;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;
    private Instant createdAt;
    private Instant updatedAt;
}

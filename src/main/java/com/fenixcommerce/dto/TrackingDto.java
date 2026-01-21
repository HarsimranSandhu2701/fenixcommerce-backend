package com.fenixcommerce.dto;

import com.fenixcommerce.entity.TrackingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrackingDto {
    private UUID id;
    private UUID fulfillmentId;
    private String trackingNumber;
    private String carrier;
    private String trackingUrl;
    private TrackingStatus status;
    private Boolean isPrimary;
    private Instant lastEventAt;
    private Instant createdAt;
    private Instant updatedAt;
}

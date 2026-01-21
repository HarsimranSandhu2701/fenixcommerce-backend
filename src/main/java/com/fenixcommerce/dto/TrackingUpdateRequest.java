package com.fenixcommerce.dto;

import com.fenixcommerce.entity.TrackingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrackingUpdateRequest {
    @NotNull(message = "Tracking number is required")
    private String trackingNumber;
    
    private String carrier;
    private String trackingUrl;
    private TrackingStatus status;
    private Boolean isPrimary = false;
    private Instant lastEventAt;
}

package com.fenixcommerce.dto;

import com.fenixcommerce.entity.TrackingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrackingPatchRequest {
    private String carrier;
    private String trackingUrl;
    private TrackingStatus status;
    private Boolean isPrimary;
    private Instant lastEventAt;
}

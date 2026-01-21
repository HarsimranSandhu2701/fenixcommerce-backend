package com.fenixcommerce.dto;

import com.fenixcommerce.entity.FulfillmentTrackingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FulfillmentPatchRequest {
    private FulfillmentTrackingStatus status;
    private String carrier;
    private String serviceLevel;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;
}

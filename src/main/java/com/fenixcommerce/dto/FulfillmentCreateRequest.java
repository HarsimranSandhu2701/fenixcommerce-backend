package com.fenixcommerce.dto;

import com.fenixcommerce.entity.FulfillmentTrackingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FulfillmentCreateRequest {
    @NotNull(message = "External Fulfillment ID is required")
    private String externalFulfillmentId;
    
    private FulfillmentTrackingStatus status;
    private String carrier;
    private String serviceLevel;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;
}

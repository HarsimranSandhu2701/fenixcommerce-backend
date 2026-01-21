package com.fenixcommerce.dto;

import com.fenixcommerce.entity.FinancialStatus;
import com.fenixcommerce.entity.FulfillmentStatus;
import com.fenixcommerce.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderUpdateRequest {
    @NotNull(message = "Organization ID is required")
    private UUID orgId;
    
    @NotNull(message = "Website ID is required")
    private UUID websiteId;
    
    @NotNull(message = "External Order ID is required")
    private String externalOrderId;
    
    private String externalOrderNumber;
    private OrderStatus status;
    private FinancialStatus financialStatus;
    private FulfillmentStatus fulfillmentStatus;
    private String customerEmail;
    private BigDecimal orderTotal;
    private String currency;
    private LocalDateTime orderCreatedAt;
    private LocalDateTime orderUpdatedAt;
}

package com.fenixcommerce.dto;

import com.fenixcommerce.entity.FinancialStatus;
import com.fenixcommerce.entity.FulfillmentStatus;
import com.fenixcommerce.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private UUID id;
    private UUID orgId;
    private UUID websiteId;
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
    private Instant ingestedAt;
    private Instant createdAt;
    private Instant updatedAt;
}

package com.fenixcommerce.dto;

import com.fenixcommerce.entity.FinancialStatus;
import com.fenixcommerce.entity.FulfillmentStatus;
import com.fenixcommerce.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPatchRequest {
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

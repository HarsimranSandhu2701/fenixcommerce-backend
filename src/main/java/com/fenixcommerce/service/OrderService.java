package com.fenixcommerce.service;

import com.fenixcommerce.dto.*;
import com.fenixcommerce.entity.FinancialStatus;
import com.fenixcommerce.entity.FulfillmentStatus;
import com.fenixcommerce.entity.OrderStatus;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.UUID;

public interface OrderService {
    OrderDto createOrder(OrderCreateRequest request);
    PageResponse<OrderDto> searchOrders(UUID orgId, UUID websiteId, Instant from, Instant to, OrderStatus status, FinancialStatus financialStatus, FulfillmentStatus fulfillmentStatus, Pageable pageable);
    PageResponse<OrderDto> searchOrdersByExternal(UUID orgId, UUID websiteId, String externalOrderId, String externalOrderNumber, Pageable pageable);
    OrderDto getOrderById(UUID orderId);
    OrderDto updateOrder(UUID orderId, OrderUpdateRequest request);
    OrderDto patchOrder(UUID orderId, OrderPatchRequest request);
    void deleteOrder(UUID orderId);
}

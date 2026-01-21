package com.fenixcommerce.controller;

import com.fenixcommerce.dto.*;
import com.fenixcommerce.entity.FinancialStatus;
import com.fenixcommerce.entity.FulfillmentStatus;
import com.fenixcommerce.entity.OrderStatus;
import com.fenixcommerce.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody OrderCreateRequest request) {
        OrderDto response = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PageResponse<OrderDto>> searchOrders(
            @RequestParam UUID orgId,
            @RequestParam(required = false) UUID websiteId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) FinancialStatus financialStatus,
            @RequestParam(required = false) FulfillmentStatus fulfillmentStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(defaultValue = "updatedAt,desc") String sort) {
        
        String[] sortParams = sort.split(",");
        Sort.Direction direction = sortParams.length > 1 && "asc".equalsIgnoreCase(sortParams[1]) 
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortParams[0]));
        
        PageResponse<OrderDto> response = orderService.searchOrders(orgId, websiteId, from, to, status, financialStatus, fulfillmentStatus, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<OrderDto>> searchOrderByExternal(
            @RequestParam UUID orgId,
            @RequestParam(required = false) UUID websiteId,
            @RequestParam(required = false) String externalOrderId,
            @RequestParam(required = false) String externalOrderNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<OrderDto> response = orderService.searchOrdersByExternal(orgId, websiteId, externalOrderId, externalOrderNumber, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable UUID orderId) {
        OrderDto response = orderService.getOrderById(orderId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderDto> updateOrder(
            @PathVariable UUID orderId,
            @Valid @RequestBody OrderUpdateRequest request) {
        OrderDto response = orderService.updateOrder(orderId, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<OrderDto> patchOrder(
            @PathVariable UUID orderId,
            @Valid @RequestBody OrderPatchRequest request) {
        OrderDto response = orderService.patchOrder(orderId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}

package com.fenixcommerce.controller;

import com.fenixcommerce.dto.*;
import com.fenixcommerce.entity.FulfillmentTrackingStatus;
import com.fenixcommerce.service.FulfillmentService;
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
@RequestMapping("/orders/{orderId}/fulfillments")
@RequiredArgsConstructor
public class FulfillmentController {

    private final FulfillmentService fulfillmentService;

    @PostMapping
    public ResponseEntity<FulfillmentDto> createFulfillment(
            @PathVariable UUID orderId,
            @Valid @RequestBody FulfillmentCreateRequest request) {
        FulfillmentDto response = fulfillmentService.createFulfillment(orderId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PageResponse<FulfillmentDto>> listFulfillments(
            @PathVariable UUID orderId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
            @RequestParam(required = false) FulfillmentTrackingStatus status,
            @RequestParam(required = false) String carrier,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(defaultValue = "updatedAt,desc") String sort) {
        
        String[] sortParams = sort.split(",");
        Sort.Direction direction = sortParams.length > 1 && "asc".equalsIgnoreCase(sortParams[1]) 
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortParams[0]));
        
        PageResponse<FulfillmentDto> response = fulfillmentService.listFulfillments(orderId, from, to, status, carrier, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<FulfillmentDto>> searchFulfillmentsByExternal(
            @PathVariable UUID orderId,
            @RequestParam String externalFulfillmentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<FulfillmentDto> response = fulfillmentService.searchFulfillmentsByExternal(orderId, externalFulfillmentId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{fulfillmentId}")
    public ResponseEntity<FulfillmentDto> getFulfillmentById(
            @PathVariable UUID orderId,
            @PathVariable UUID fulfillmentId) {
        FulfillmentDto response = fulfillmentService.getFulfillmentById(orderId, fulfillmentId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{fulfillmentId}")
    public ResponseEntity<FulfillmentDto> updateFulfillment(
            @PathVariable UUID orderId,
            @PathVariable UUID fulfillmentId,
            @Valid @RequestBody FulfillmentUpdateRequest request) {
        FulfillmentDto response = fulfillmentService.updateFulfillment(orderId, fulfillmentId, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{fulfillmentId}")
    public ResponseEntity<FulfillmentDto> patchFulfillment(
            @PathVariable UUID orderId,
            @PathVariable UUID fulfillmentId,
            @Valid @RequestBody FulfillmentPatchRequest request) {
        FulfillmentDto response = fulfillmentService.patchFulfillment(orderId, fulfillmentId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{fulfillmentId}")
    public ResponseEntity<Void> deleteFulfillment(
            @PathVariable UUID orderId,
            @PathVariable UUID fulfillmentId) {
        fulfillmentService.deleteFulfillment(orderId, fulfillmentId);
        return ResponseEntity.noContent().build();
    }
}

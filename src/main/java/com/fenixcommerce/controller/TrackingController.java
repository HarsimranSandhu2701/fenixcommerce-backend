package com.fenixcommerce.controller;

import com.fenixcommerce.dto.*;
import com.fenixcommerce.entity.TrackingStatus;
import com.fenixcommerce.service.TrackingService;
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
@RequestMapping("/fulfillments/{fulfillmentId}/tracking")
@RequiredArgsConstructor
public class TrackingController {

    private final TrackingService trackingService;

    @PostMapping
    public ResponseEntity<TrackingDto> createTracking(
            @PathVariable UUID fulfillmentId,
            @Valid @RequestBody TrackingCreateRequest request) {
        TrackingDto response = trackingService.createTracking(fulfillmentId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PageResponse<TrackingDto>> listTracking(
            @PathVariable UUID fulfillmentId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
            @RequestParam(required = false) TrackingStatus status,
            @RequestParam(required = false) String carrier,
            @RequestParam(required = false) String trackingNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(defaultValue = "updatedAt,desc") String sort) {
        
        String[] sortParams = sort.split(",");
        Sort.Direction direction = sortParams.length > 1 && "asc".equalsIgnoreCase(sortParams[1]) 
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortParams[0]));
        
        PageResponse<TrackingDto> response = trackingService.listTracking(fulfillmentId, from, to, status, carrier, trackingNumber, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<TrackingDto>> searchTrackingByNumber(
            @PathVariable UUID fulfillmentId,
            @RequestParam String trackingNumber,
            @RequestParam(required = false) String carrier,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<TrackingDto> response = trackingService.searchTrackingByNumber(fulfillmentId, trackingNumber, carrier, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{trackingId}")
    public ResponseEntity<TrackingDto> getTrackingById(
            @PathVariable UUID fulfillmentId,
            @PathVariable UUID trackingId) {
        TrackingDto response = trackingService.getTrackingById(fulfillmentId, trackingId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{trackingId}")
    public ResponseEntity<TrackingDto> updateTracking(
            @PathVariable UUID fulfillmentId,
            @PathVariable UUID trackingId,
            @Valid @RequestBody TrackingUpdateRequest request) {
        TrackingDto response = trackingService.updateTracking(fulfillmentId, trackingId, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{trackingId}")
    public ResponseEntity<TrackingDto> patchTracking(
            @PathVariable UUID fulfillmentId,
            @PathVariable UUID trackingId,
            @Valid @RequestBody TrackingPatchRequest request) {
        TrackingDto response = trackingService.patchTracking(fulfillmentId, trackingId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{trackingId}")
    public ResponseEntity<Void> deleteTracking(
            @PathVariable UUID fulfillmentId,
            @PathVariable UUID trackingId) {
        trackingService.deleteTracking(fulfillmentId, trackingId);
        return ResponseEntity.noContent().build();
    }
}

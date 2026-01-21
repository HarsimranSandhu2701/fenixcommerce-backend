package com.fenixcommerce.service;

import com.fenixcommerce.dto.*;
import com.fenixcommerce.entity.TrackingStatus;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.UUID;

public interface TrackingService {
    TrackingDto createTracking(UUID fulfillmentId, TrackingCreateRequest request);
    PageResponse<TrackingDto> listTracking(UUID fulfillmentId, Instant from, Instant to, TrackingStatus status, String carrier, String trackingNumber, Pageable pageable);
    PageResponse<TrackingDto> searchTrackingByNumber(UUID fulfillmentId, String trackingNumber, String carrier, Pageable pageable);
    TrackingDto getTrackingById(UUID fulfillmentId, UUID trackingId);
    TrackingDto updateTracking(UUID fulfillmentId, UUID trackingId, TrackingUpdateRequest request);
    TrackingDto patchTracking(UUID fulfillmentId, UUID trackingId, TrackingPatchRequest request);
    void deleteTracking(UUID fulfillmentId, UUID trackingId);
}

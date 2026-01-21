package com.fenixcommerce.service;

import com.fenixcommerce.dto.*;
import com.fenixcommerce.entity.FulfillmentTrackingStatus;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.UUID;

public interface FulfillmentService {
    FulfillmentDto createFulfillment(UUID orderId, FulfillmentCreateRequest request);
    PageResponse<FulfillmentDto> listFulfillments(UUID orderId, Instant from, Instant to, FulfillmentTrackingStatus status, String carrier, Pageable pageable);
    PageResponse<FulfillmentDto> searchFulfillmentsByExternal(UUID orderId, String externalFulfillmentId, Pageable pageable);
    FulfillmentDto getFulfillmentById(UUID orderId, UUID fulfillmentId);
    FulfillmentDto updateFulfillment(UUID orderId, UUID fulfillmentId, FulfillmentUpdateRequest request);
    FulfillmentDto patchFulfillment(UUID orderId, UUID fulfillmentId, FulfillmentPatchRequest request);
    void deleteFulfillment(UUID orderId, UUID fulfillmentId);
}

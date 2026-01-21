package com.fenixcommerce.service.impl;

import com.fenixcommerce.dto.*;
import com.fenixcommerce.entity.Fulfillment;
import com.fenixcommerce.entity.Tracking;
import com.fenixcommerce.entity.TrackingStatus;
import com.fenixcommerce.exception.ResourceNotFoundException;
import com.fenixcommerce.repository.FulfillmentRepository;
import com.fenixcommerce.repository.TrackingRepository;
import com.fenixcommerce.service.TrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TrackingServiceImpl implements TrackingService {

    private final TrackingRepository trackingRepository;
    private final FulfillmentRepository fulfillmentRepository;

    @Override
    public TrackingDto createTracking(UUID fulfillmentId, TrackingCreateRequest request) {
        Fulfillment fulfillment = fulfillmentRepository.findById(fulfillmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Fulfillment not found with id: " + fulfillmentId));
        
        Tracking tracking = new Tracking();
        tracking.setId(UUID.randomUUID());
        tracking.setOrganization(fulfillment.getOrganization());
        tracking.setFulfillment(fulfillment);
        tracking.setTrackingNumber(request.getTrackingNumber());
        tracking.setCarrier(request.getCarrier());
        tracking.setTrackingUrl(request.getTrackingUrl());
        tracking.setStatus(request.getStatus() != null ? request.getStatus() : TrackingStatus.UNKNOWN);
        tracking.setIsPrimary(request.getIsPrimary() != null ? request.getIsPrimary() : false);
        tracking.setLastEventAt(request.getLastEventAt());
        
        Tracking saved = trackingRepository.save(tracking);
        return mapToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<TrackingDto> listTracking(UUID fulfillmentId, Instant from, Instant to, TrackingStatus status, String carrier, String trackingNumber, Pageable pageable) {
        Fulfillment fulfillment = fulfillmentRepository.findById(fulfillmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Fulfillment not found with id: " + fulfillmentId));
        
        Page<Tracking> page = trackingRepository.searchTracking(fulfillment, from, to, status, carrier, trackingNumber, pageable);
        return mapToPageResponse(page);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<TrackingDto> searchTrackingByNumber(UUID fulfillmentId, String trackingNumber, String carrier, Pageable pageable) {
        Fulfillment fulfillment = fulfillmentRepository.findById(fulfillmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Fulfillment not found with id: " + fulfillmentId));
        
        Page<Tracking> page = trackingRepository.findByFulfillmentAndTrackingNumber(fulfillment, trackingNumber)
                .map(t -> new org.springframework.data.domain.PageImpl<>(java.util.List.of(t), pageable, 1))
                .orElse(new org.springframework.data.domain.PageImpl<>(java.util.List.of(), pageable, 0));
        
        return mapToPageResponse(page);
    }

    @Override
    @Transactional(readOnly = true)
    public TrackingDto getTrackingById(UUID fulfillmentId, UUID trackingId) {
        Fulfillment fulfillment = fulfillmentRepository.findById(fulfillmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Fulfillment not found with id: " + fulfillmentId));
        
        Tracking tracking = trackingRepository.findByFulfillmentAndId(fulfillment, trackingId)
                .orElseThrow(() -> new ResourceNotFoundException("Tracking not found with id: " + trackingId));
        return mapToDto(tracking);
    }

    @Override
    public TrackingDto updateTracking(UUID fulfillmentId, UUID trackingId, TrackingUpdateRequest request) {
        Fulfillment fulfillment = fulfillmentRepository.findById(fulfillmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Fulfillment not found with id: " + fulfillmentId));
        
        Tracking tracking = trackingRepository.findByFulfillmentAndId(fulfillment, trackingId)
                .orElseThrow(() -> new ResourceNotFoundException("Tracking not found with id: " + trackingId));
        
        tracking.setTrackingNumber(request.getTrackingNumber());
        tracking.setCarrier(request.getCarrier());
        tracking.setTrackingUrl(request.getTrackingUrl());
        tracking.setStatus(request.getStatus() != null ? request.getStatus() : TrackingStatus.UNKNOWN);
        tracking.setIsPrimary(request.getIsPrimary() != null ? request.getIsPrimary() : false);
        tracking.setLastEventAt(request.getLastEventAt());
        
        Tracking saved = trackingRepository.save(tracking);
        return mapToDto(saved);
    }

    @Override
    public TrackingDto patchTracking(UUID fulfillmentId, UUID trackingId, TrackingPatchRequest request) {
        Fulfillment fulfillment = fulfillmentRepository.findById(fulfillmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Fulfillment not found with id: " + fulfillmentId));
        
        Tracking tracking = trackingRepository.findByFulfillmentAndId(fulfillment, trackingId)
                .orElseThrow(() -> new ResourceNotFoundException("Tracking not found with id: " + trackingId));
        
        if (request.getCarrier() != null) tracking.setCarrier(request.getCarrier());
        if (request.getTrackingUrl() != null) tracking.setTrackingUrl(request.getTrackingUrl());
        if (request.getStatus() != null) tracking.setStatus(request.getStatus());
        if (request.getIsPrimary() != null) tracking.setIsPrimary(request.getIsPrimary());
        if (request.getLastEventAt() != null) tracking.setLastEventAt(request.getLastEventAt());
        
        Tracking saved = trackingRepository.save(tracking);
        return mapToDto(saved);
    }

    @Override
    public void deleteTracking(UUID fulfillmentId, UUID trackingId) {
        Fulfillment fulfillment = fulfillmentRepository.findById(fulfillmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Fulfillment not found with id: " + fulfillmentId));
        
        Tracking tracking = trackingRepository.findByFulfillmentAndId(fulfillment, trackingId)
                .orElseThrow(() -> new ResourceNotFoundException("Tracking not found with id: " + trackingId));
        trackingRepository.delete(tracking);
    }

    private TrackingDto mapToDto(Tracking tracking) {
        TrackingDto dto = new TrackingDto();
        dto.setId(tracking.getId());
        dto.setFulfillmentId(tracking.getFulfillment().getFulfillmentId());
        dto.setTrackingNumber(tracking.getTrackingNumber());
        dto.setCarrier(tracking.getCarrier());
        dto.setTrackingUrl(tracking.getTrackingUrl());
        dto.setStatus(tracking.getStatus());
        dto.setIsPrimary(tracking.getIsPrimary());
        dto.setLastEventAt(tracking.getLastEventAt());
        dto.setCreatedAt(tracking.getCreatedAt());
        dto.setUpdatedAt(tracking.getUpdatedAt());
        return dto;
    }

    private PageResponse<TrackingDto> mapToPageResponse(Page<Tracking> page) {
        PageResponse<TrackingDto> response = new PageResponse<>();
        response.setData(page.getContent().stream().map(this::mapToDto).collect(Collectors.toList()));
        response.setPage(page.getNumber());
        response.setSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setHasNext(page.hasNext());
        return response;
    }
}

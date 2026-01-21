package com.fenixcommerce.service.impl;

import com.fenixcommerce.dto.*;
import com.fenixcommerce.entity.Fulfillment;
import com.fenixcommerce.entity.FulfillmentTrackingStatus;
import com.fenixcommerce.entity.Order;
import com.fenixcommerce.exception.ResourceNotFoundException;
import com.fenixcommerce.repository.FulfillmentRepository;
import com.fenixcommerce.repository.OrderRepository;
import com.fenixcommerce.service.FulfillmentService;
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
public class FulfillmentServiceImpl implements FulfillmentService {

    private final FulfillmentRepository fulfillmentRepository;
    private final OrderRepository orderRepository;

    @Override
    public FulfillmentDto createFulfillment(UUID orderId, FulfillmentCreateRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        
        Fulfillment fulfillment = new Fulfillment();
        fulfillment.setFulfillmentId(UUID.randomUUID());
        fulfillment.setOrganization(order.getOrganization());
        fulfillment.setOrder(order);
        fulfillment.setExternalFulfillmentId(request.getExternalFulfillmentId());
        fulfillment.setFulfillmentStatus(request.getStatus() != null ? request.getStatus() : FulfillmentTrackingStatus.CREATED);
        fulfillment.setCarrier(request.getCarrier());
        fulfillment.setServiceLevel(request.getServiceLevel());
        fulfillment.setShippedAt(request.getShippedAt());
        fulfillment.setDeliveredAt(request.getDeliveredAt());
        
        Fulfillment saved = fulfillmentRepository.save(fulfillment);
        return mapToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<FulfillmentDto> listFulfillments(UUID orderId, Instant from, Instant to, FulfillmentTrackingStatus status, String carrier, Pageable pageable) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        
        Page<Fulfillment> page = fulfillmentRepository.searchFulfillments(order, from, to, status, carrier, pageable);
        return mapToPageResponse(page);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<FulfillmentDto> searchFulfillmentsByExternal(UUID orderId, String externalFulfillmentId, Pageable pageable) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        
        Page<Fulfillment> page = fulfillmentRepository.findByOrderAndExternalFulfillmentId(order, externalFulfillmentId)
                .map(f -> new org.springframework.data.domain.PageImpl<>(java.util.List.of(f), pageable, 1))
                .orElse(new org.springframework.data.domain.PageImpl<>(java.util.List.of(), pageable, 0));
        
        return mapToPageResponse(page);
    }

    @Override
    @Transactional(readOnly = true)
    public FulfillmentDto getFulfillmentById(UUID orderId, UUID fulfillmentId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        
        Fulfillment fulfillment = fulfillmentRepository.findByOrderAndId(order, fulfillmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Fulfillment not found with id: " + fulfillmentId));
        return mapToDto(fulfillment);
    }

    @Override
    public FulfillmentDto updateFulfillment(UUID orderId, UUID fulfillmentId, FulfillmentUpdateRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        
        Fulfillment fulfillment = fulfillmentRepository.findByOrderAndId(order, fulfillmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Fulfillment not found with id: " + fulfillmentId));
        
        fulfillment.setExternalFulfillmentId(request.getExternalFulfillmentId());
        fulfillment.setFulfillmentStatus(request.getStatus() != null ? request.getStatus() : FulfillmentTrackingStatus.CREATED);
        fulfillment.setCarrier(request.getCarrier());
        fulfillment.setServiceLevel(request.getServiceLevel());
        fulfillment.setShippedAt(request.getShippedAt());
        fulfillment.setDeliveredAt(request.getDeliveredAt());
        
        Fulfillment saved = fulfillmentRepository.save(fulfillment);
        return mapToDto(saved);
    }

    @Override
    public FulfillmentDto patchFulfillment(UUID orderId, UUID fulfillmentId, FulfillmentPatchRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        
        Fulfillment fulfillment = fulfillmentRepository.findByOrderAndId(order, fulfillmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Fulfillment not found with id: " + fulfillmentId));
        
        if (request.getStatus() != null) fulfillment.setFulfillmentStatus(request.getStatus());
        if (request.getCarrier() != null) fulfillment.setCarrier(request.getCarrier());
        if (request.getServiceLevel() != null) fulfillment.setServiceLevel(request.getServiceLevel());
        if (request.getShippedAt() != null) fulfillment.setShippedAt(request.getShippedAt());
        if (request.getDeliveredAt() != null) fulfillment.setDeliveredAt(request.getDeliveredAt());
        
        Fulfillment saved = fulfillmentRepository.save(fulfillment);
        return mapToDto(saved);
    }

    @Override
    public void deleteFulfillment(UUID orderId, UUID fulfillmentId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        
        Fulfillment fulfillment = fulfillmentRepository.findByOrderAndId(order, fulfillmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Fulfillment not found with id: " + fulfillmentId));
        fulfillmentRepository.delete(fulfillment);
    }

    private FulfillmentDto mapToDto(Fulfillment fulfillment) {
        FulfillmentDto dto = new FulfillmentDto();
        dto.setId(fulfillment.getFulfillmentId());
        dto.setOrderId(fulfillment.getOrder().getOrderId());
        dto.setExternalFulfillmentId(fulfillment.getExternalFulfillmentId());
        dto.setStatus(fulfillment.getFulfillmentStatus());
        dto.setCarrier(fulfillment.getCarrier());
        dto.setServiceLevel(fulfillment.getServiceLevel());
        dto.setShippedAt(fulfillment.getShippedAt());
        dto.setDeliveredAt(fulfillment.getDeliveredAt());
        dto.setCreatedAt(fulfillment.getCreatedAt());
        dto.setUpdatedAt(fulfillment.getUpdatedAt());
        return dto;
    }

    private PageResponse<FulfillmentDto> mapToPageResponse(Page<Fulfillment> page) {
        PageResponse<FulfillmentDto> response = new PageResponse<>();
        response.setData(page.getContent().stream().map(this::mapToDto).collect(Collectors.toList()));
        response.setPage(page.getNumber());
        response.setSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setHasNext(page.hasNext());
        return response;
    }
}

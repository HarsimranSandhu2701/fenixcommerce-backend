package com.fenixcommerce.service.impl;

import com.fenixcommerce.dto.*;
import com.fenixcommerce.entity.*;
import com.fenixcommerce.exception.ResourceNotFoundException;
import com.fenixcommerce.repository.OrganizationRepository;
import com.fenixcommerce.repository.OrderRepository;
import com.fenixcommerce.repository.WebsiteRepository;
import com.fenixcommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrganizationRepository organizationRepository;
    private final WebsiteRepository websiteRepository;

    @Override
    public OrderDto createOrder(OrderCreateRequest request) {
        // Check if order exists for upsert
        Order order = orderRepository
                .findByOrganization_IdAndWebsite_IdAndExternalOrderId(
                        request.getOrgId(), request.getWebsiteId(), request.getExternalOrderId())
                .orElse(null);

        if (order == null) {
            // Create new order
            Organization organization = organizationRepository.findById(request.getOrgId())
                    .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + request.getOrgId()));
            
            Website website = websiteRepository.findById(request.getWebsiteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Website not found with id: " + request.getWebsiteId()));

            order = new Order();
            order.setOrderId(UUID.randomUUID());
            order.setOrganization(organization);
            order.setWebsite(website);
            order.setExternalOrderId(request.getExternalOrderId());
            order.setExternalOrderNumber(request.getExternalOrderNumber());
            order.setOrderStatus(request.getStatus() != null ? request.getStatus() : OrderStatus.CREATED);
            order.setFinancialStatus(request.getFinancialStatus() != null ? request.getFinancialStatus() : FinancialStatus.UNKNOWN);
            order.setFulfillmentStatus(request.getFulfillmentStatus() != null ? request.getFulfillmentStatus() : FulfillmentStatus.UNFULFILLED);
            order.setCustomerEmail(request.getCustomerEmail());
            order.setOrderTotal(request.getOrderTotal() != null ? request.getOrderTotal() : java.math.BigDecimal.ZERO);
            order.setCurrency(request.getCurrency());
            order.setOrderCreatedAt(request.getOrderCreatedAt() != null ? request.getOrderCreatedAt() : LocalDateTime.now());
            order.setOrderUpdatedAt(request.getOrderUpdatedAt() != null ? request.getOrderUpdatedAt() : LocalDateTime.now());
            order.setIngestedAt(Instant.now());
        } else {
            // Update existing order
            if (request.getExternalOrderNumber() != null) order.setExternalOrderNumber(request.getExternalOrderNumber());
            if (request.getStatus() != null) order.setOrderStatus(request.getStatus());
            if (request.getFinancialStatus() != null) order.setFinancialStatus(request.getFinancialStatus());
            if (request.getFulfillmentStatus() != null) order.setFulfillmentStatus(request.getFulfillmentStatus());
            if (request.getCustomerEmail() != null) order.setCustomerEmail(request.getCustomerEmail());
            if (request.getOrderTotal() != null) order.setOrderTotal(request.getOrderTotal());
            if (request.getCurrency() != null) order.setCurrency(request.getCurrency());
            if (request.getOrderCreatedAt() != null) order.setOrderCreatedAt(request.getOrderCreatedAt());
            if (request.getOrderUpdatedAt() != null) order.setOrderUpdatedAt(request.getOrderUpdatedAt());
            order.setIngestedAt(Instant.now());
        }

        Order saved = orderRepository.save(order);
        return mapToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<OrderDto> searchOrders(UUID orgId, UUID websiteId, Instant from, Instant to, OrderStatus status, FinancialStatus financialStatus, FulfillmentStatus fulfillmentStatus, Pageable pageable) {
        Page<Order> page = orderRepository.searchOrders(orgId, websiteId, from, to, status, financialStatus, fulfillmentStatus, pageable);
        return mapToPageResponse(page);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<OrderDto> searchOrdersByExternal(UUID orgId, UUID websiteId, String externalOrderId, String externalOrderNumber, Pageable pageable) {
        Page<Order> page = orderRepository.searchOrdersByExternal(orgId, websiteId, externalOrderId, externalOrderNumber, pageable);
        return mapToPageResponse(page);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrderById(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        return mapToDto(order);
    }

    @Override
    public OrderDto updateOrder(UUID orderId, OrderUpdateRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        
        Organization organization = organizationRepository.findById(request.getOrgId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + request.getOrgId()));
        
        Website website = websiteRepository.findById(request.getWebsiteId())
                .orElseThrow(() -> new ResourceNotFoundException("Website not found with id: " + request.getWebsiteId()));

        order.setOrganization(organization);
        order.setWebsite(website);
        order.setExternalOrderId(request.getExternalOrderId());
        order.setExternalOrderNumber(request.getExternalOrderNumber());
        order.setOrderStatus(request.getStatus() != null ? request.getStatus() : OrderStatus.CREATED);
        order.setFinancialStatus(request.getFinancialStatus() != null ? request.getFinancialStatus() : FinancialStatus.UNKNOWN);
        order.setFulfillmentStatus(request.getFulfillmentStatus() != null ? request.getFulfillmentStatus() : FulfillmentStatus.UNFULFILLED);
        order.setCustomerEmail(request.getCustomerEmail());
        order.setOrderTotal(request.getOrderTotal() != null ? request.getOrderTotal() : java.math.BigDecimal.ZERO);
        order.setCurrency(request.getCurrency());
        order.setOrderCreatedAt(request.getOrderCreatedAt() != null ? request.getOrderCreatedAt() : LocalDateTime.now());
        order.setOrderUpdatedAt(request.getOrderUpdatedAt() != null ? request.getOrderUpdatedAt() : LocalDateTime.now());
        order.setIngestedAt(Instant.now());

        Order saved = orderRepository.save(order);
        return mapToDto(saved);
    }

    @Override
    public OrderDto patchOrder(UUID orderId, OrderPatchRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        
        if (request.getExternalOrderNumber() != null) order.setExternalOrderNumber(request.getExternalOrderNumber());
        if (request.getStatus() != null) order.setOrderStatus(request.getStatus());
        if (request.getFinancialStatus() != null) order.setFinancialStatus(request.getFinancialStatus());
        if (request.getFulfillmentStatus() != null) order.setFulfillmentStatus(request.getFulfillmentStatus());
        if (request.getCustomerEmail() != null) order.setCustomerEmail(request.getCustomerEmail());
        if (request.getOrderTotal() != null) order.setOrderTotal(request.getOrderTotal());
        if (request.getCurrency() != null) order.setCurrency(request.getCurrency());
        if (request.getOrderCreatedAt() != null) order.setOrderCreatedAt(request.getOrderCreatedAt());
        if (request.getOrderUpdatedAt() != null) order.setOrderUpdatedAt(request.getOrderUpdatedAt());

        Order saved = orderRepository.save(order);
        return mapToDto(saved);
    }

    @Override
    public void deleteOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        orderRepository.delete(order);
    }

    private OrderDto mapToDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getOrderId());
        dto.setOrgId(order.getOrganization().getId());
        dto.setWebsiteId(order.getWebsite().getId());
        dto.setExternalOrderId(order.getExternalOrderId());
        dto.setExternalOrderNumber(order.getExternalOrderNumber());
        dto.setStatus(order.getOrderStatus());
        dto.setFinancialStatus(order.getFinancialStatus());
        dto.setFulfillmentStatus(order.getFulfillmentStatus());
        dto.setCustomerEmail(order.getCustomerEmail());
        dto.setOrderTotal(order.getOrderTotal());
        dto.setCurrency(order.getCurrency());
        dto.setOrderCreatedAt(order.getOrderCreatedAt());
        dto.setOrderUpdatedAt(order.getOrderUpdatedAt());
        dto.setIngestedAt(order.getIngestedAt());
        // Map ingestedAt to createdAt/updatedAt for API response (per OpenAPI spec)
        dto.setCreatedAt(order.getIngestedAt());
        dto.setUpdatedAt(order.getIngestedAt());
        return dto;
    }

    private PageResponse<OrderDto> mapToPageResponse(Page<Order> page) {
        PageResponse<OrderDto> response = new PageResponse<>();
        response.setData(page.getContent().stream().map(this::mapToDto).collect(Collectors.toList()));
        response.setPage(page.getNumber());
        response.setSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setHasNext(page.hasNext());
        return response;
    }
}

package com.fenixcommerce.repository;

import com.fenixcommerce.entity.FinancialStatus;
import com.fenixcommerce.entity.FulfillmentStatus;
import com.fenixcommerce.entity.Order;
import com.fenixcommerce.entity.OrderStatus;
import com.fenixcommerce.entity.Organization;
import com.fenixcommerce.entity.Website;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    
    Optional<Order> findByOrganization_IdAndWebsite_IdAndExternalOrderId(
            UUID orgId, UUID websiteId, String externalOrderId);
    
    Page<Order> findByOrganization(Organization organization, Pageable pageable);
    
    Page<Order> findByOrganizationAndWebsite(Organization organization, Website website, Pageable pageable);
    
    @Query("SELECT o FROM Order o WHERE o.organization.id = :orgId AND " +
           "(:websiteId IS NULL OR o.website.id = :websiteId) AND " +
           "(:from IS NULL OR o.createdAt >= :from) AND " +
           "(:to IS NULL OR o.createdAt <= :to) AND " +
           "(:status IS NULL OR o.orderStatus = :status) AND " +
           "(:financialStatus IS NULL OR o.financialStatus = :financialStatus) AND " +
           "(:fulfillmentStatus IS NULL OR o.fulfillmentStatus = :fulfillmentStatus)")
    Page<Order> searchOrders(@Param("orgId") UUID orgId,
                             @Param("websiteId") UUID websiteId,
                             @Param("from") Instant from,
                             @Param("to") Instant to,
                             @Param("status") OrderStatus status,
                             @Param("financialStatus") FinancialStatus financialStatus,
                             @Param("fulfillmentStatus") FulfillmentStatus fulfillmentStatus,
                             Pageable pageable);
    
    @Query("SELECT o FROM Order o WHERE o.organization.id = :orgId AND " +
           "(:websiteId IS NULL OR o.website.id = :websiteId) AND " +
           "(:externalOrderId IS NULL OR o.externalOrderId = :externalOrderId) AND " +
           "(:externalOrderNumber IS NULL OR o.externalOrderNumber = :externalOrderNumber)")
    Page<Order> searchOrdersByExternal(@Param("orgId") UUID orgId,
                                       @Param("websiteId") UUID websiteId,
                                       @Param("externalOrderId") String externalOrderId,
                                       @Param("externalOrderNumber") String externalOrderNumber,
                                       Pageable pageable);
}

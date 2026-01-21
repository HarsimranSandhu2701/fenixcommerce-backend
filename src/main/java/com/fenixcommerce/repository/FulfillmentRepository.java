package com.fenixcommerce.repository;

import com.fenixcommerce.entity.Fulfillment;
import com.fenixcommerce.entity.FulfillmentTrackingStatus;
import com.fenixcommerce.entity.Order;
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
public interface FulfillmentRepository extends JpaRepository<Fulfillment, UUID> {
    
    Page<Fulfillment> findByOrder(Order order, Pageable pageable);
    
    Optional<Fulfillment> findByOrderAndId(Order order, UUID id);
    
    Optional<Fulfillment> findByOrderAndExternalFulfillmentId(Order order, String externalFulfillmentId);
    
    @Query("SELECT f FROM Fulfillment f WHERE f.order = :order AND " +
           "(:from IS NULL OR f.createdAt >= :from) AND " +
           "(:to IS NULL OR f.createdAt <= :to) AND " +
           "(:status IS NULL OR f.fulfillmentStatus = :status) AND " +
           "(:carrier IS NULL OR f.carrier = :carrier)")
    Page<Fulfillment> searchFulfillments(@Param("order") Order order,
                                         @Param("from") Instant from,
                                         @Param("to") Instant to,
                                         @Param("status") FulfillmentTrackingStatus status,
                                         @Param("carrier") String carrier,
                                         Pageable pageable);
}

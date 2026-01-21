package com.fenixcommerce.repository;

import com.fenixcommerce.entity.Fulfillment;
import com.fenixcommerce.entity.Tracking;
import com.fenixcommerce.entity.TrackingStatus;
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
public interface TrackingRepository extends JpaRepository<Tracking, UUID> {
    
    Page<Tracking> findByFulfillment(Fulfillment fulfillment, Pageable pageable);
    
    Optional<Tracking> findByFulfillmentAndId(Fulfillment fulfillment, UUID id);
    
    Optional<Tracking> findByFulfillmentAndTrackingNumber(Fulfillment fulfillment, String trackingNumber);
    
    @Query("SELECT t FROM Tracking t WHERE t.fulfillment = :fulfillment AND " +
           "(:from IS NULL OR t.createdAt >= :from) AND " +
           "(:to IS NULL OR t.createdAt <= :to) AND " +
           "(:status IS NULL OR t.status = :status) AND " +
           "(:carrier IS NULL OR t.carrier = :carrier) AND " +
           "(:trackingNumber IS NULL OR t.trackingNumber = :trackingNumber)")
    Page<Tracking> searchTracking(@Param("fulfillment") Fulfillment fulfillment,
                                  @Param("from") Instant from,
                                  @Param("to") Instant to,
                                  @Param("status") TrackingStatus status,
                                  @Param("carrier") String carrier,
                                  @Param("trackingNumber") String trackingNumber,
                                  Pageable pageable);
}

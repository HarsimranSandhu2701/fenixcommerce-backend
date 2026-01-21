package com.fenixcommerce.repository;

import com.fenixcommerce.entity.OrgStatus;
import com.fenixcommerce.entity.Organization;
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
public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
    
    Page<Organization> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    @Query("SELECT o FROM Organization o WHERE " +
           "(:from IS NULL OR o.createdAt >= :from) AND " +
           "(:to IS NULL OR o.createdAt <= :to) AND " +
           "(:name IS NULL OR LOWER(o.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:status IS NULL OR o.status = :status)")
    Page<Organization> searchOrganizations(@Param("from") Instant from, 
                                          @Param("to") Instant to,
                                          @Param("name") String name,
                                          @Param("status") OrgStatus status,
                                          Pageable pageable);
}

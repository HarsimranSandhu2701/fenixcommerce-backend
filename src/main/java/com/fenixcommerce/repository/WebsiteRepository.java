package com.fenixcommerce.repository;

import com.fenixcommerce.entity.Organization;
import com.fenixcommerce.entity.Platform;
import com.fenixcommerce.entity.Website;
import com.fenixcommerce.entity.WebsiteStatus;
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
public interface WebsiteRepository extends JpaRepository<Website, UUID> {
    
    Page<Website> findByOrganization(Organization organization, Pageable pageable);
    
    Optional<Website> findByOrganizationAndId(Organization organization, UUID id);
    
    Optional<Website> findByOrganizationAndCode(Organization organization, String code);
    
    @Query("SELECT w FROM Website w WHERE w.organization = :org AND " +
           "(:from IS NULL OR w.createdAt >= :from) AND " +
           "(:to IS NULL OR w.createdAt <= :to) AND " +
           "(:status IS NULL OR w.status = :status) AND " +
           "(:platform IS NULL OR w.platform = :platform) AND " +
           "(:code IS NULL OR w.code = :code) AND " +
           "(:domain IS NULL OR w.domain = :domain)")
    Page<Website> searchWebsites(@Param("org") Organization organization,
                                  @Param("from") Instant from,
                                  @Param("to") Instant to,
                                  @Param("status") WebsiteStatus status,
                                  @Param("platform") Platform platform,
                                  @Param("code") String code,
                                  @Param("domain") String domain,
                                  Pageable pageable);
}

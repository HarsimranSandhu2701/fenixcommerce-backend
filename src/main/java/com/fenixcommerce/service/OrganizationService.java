package com.fenixcommerce.service;

import com.fenixcommerce.dto.*;
import com.fenixcommerce.entity.OrgStatus;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.UUID;

public interface OrganizationService {
    OrganizationDto createOrganization(OrganizationCreateRequest request);
    PageResponse<OrganizationDto> listOrganizations(Instant from, Instant to, String name, OrgStatus status, Pageable pageable);
    OrganizationDto getOrganizationById(UUID id);
    OrganizationDto updateOrganization(UUID id, OrganizationUpdateRequest request);
    OrganizationDto patchOrganization(UUID id, OrganizationPatchRequest request);
    void deleteOrganization(UUID id);
}

package com.fenixcommerce.service.impl;

import com.fenixcommerce.dto.*;
import com.fenixcommerce.entity.OrgStatus;
import com.fenixcommerce.entity.Organization;
import com.fenixcommerce.exception.ResourceNotFoundException;
import com.fenixcommerce.repository.OrganizationRepository;
import com.fenixcommerce.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;

    @Override
    public OrganizationDto createOrganization(OrganizationCreateRequest request) {
        Organization organization = new Organization();
        organization.setId(UUID.randomUUID());
        organization.setName(request.getName());
        organization.setStatus(request.getStatus() != null ? request.getStatus() : OrgStatus.ACTIVE);
        
        Organization saved = organizationRepository.save(organization);
        return mapToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<OrganizationDto> listOrganizations(Instant from, Instant to, String name, OrgStatus status, Pageable pageable) {
        Page<Organization> page = organizationRepository.searchOrganizations(from, to, name, status, pageable);
        return mapToPageResponse(page);
    }


    @Override
    @Transactional(readOnly = true)
    public OrganizationDto getOrganizationById(UUID id) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + id));
        return mapToDto(organization);
    }

    @Override
    public OrganizationDto updateOrganization(UUID id, OrganizationUpdateRequest request) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + id));
        
        organization.setName(request.getName());
        organization.setStatus(request.getStatus());
        
        Organization saved = organizationRepository.save(organization);
        return mapToDto(saved);
    }

    @Override
    public OrganizationDto patchOrganization(UUID id, OrganizationPatchRequest request) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + id));
        
        if (request.getName() != null) {
            organization.setName(request.getName());
        }
        if (request.getStatus() != null) {
            organization.setStatus(request.getStatus());
        }
        
        Organization saved = organizationRepository.save(organization);
        return mapToDto(saved);
    }

    @Override
    public void deleteOrganization(UUID id) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + id));
        organizationRepository.delete(organization);
    }

    private OrganizationDto mapToDto(Organization organization) {
        OrganizationDto dto = new OrganizationDto();
        dto.setId(organization.getId());
        dto.setName(organization.getName());
        dto.setStatus(organization.getStatus());
        dto.setCreatedAt(organization.getCreatedAt());
        dto.setUpdatedAt(organization.getUpdatedAt());
        return dto;
    }

    private PageResponse<OrganizationDto> mapToPageResponse(Page<Organization> page) {
        PageResponse<OrganizationDto> response = new PageResponse<>();
        response.setData(page.getContent().stream().map(this::mapToDto).collect(Collectors.toList()));
        response.setPage(page.getNumber());
        response.setSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setHasNext(page.hasNext());
        return response;
    }
}

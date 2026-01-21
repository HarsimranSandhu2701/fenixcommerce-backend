package com.fenixcommerce.service.impl;

import com.fenixcommerce.dto.*;
import com.fenixcommerce.entity.Organization;
import com.fenixcommerce.entity.Platform;
import com.fenixcommerce.entity.Website;
import com.fenixcommerce.entity.WebsiteStatus;
import com.fenixcommerce.exception.ResourceNotFoundException;
import com.fenixcommerce.repository.OrganizationRepository;
import com.fenixcommerce.repository.WebsiteRepository;
import com.fenixcommerce.service.WebsiteService;
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
public class WebsiteServiceImpl implements WebsiteService {

    private final WebsiteRepository websiteRepository;
    private final OrganizationRepository organizationRepository;

    @Override
    public WebsiteDto createWebsite(UUID orgId, WebsiteCreateRequest request) {
        Organization organization = organizationRepository.findById(orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + orgId));
        
        Website website = new Website();
        website.setId(UUID.randomUUID());
        website.setOrganization(organization);
        website.setCode(request.getCode());
        website.setName(request.getName());
        website.setPlatform(request.getPlatform());
        website.setDomain(request.getDomain());
        website.setStatus(request.getStatus() != null ? request.getStatus() : WebsiteStatus.ACTIVE);
        
        Website saved = websiteRepository.save(website);
        return mapToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<WebsiteDto> listWebsites(UUID orgId, Instant from, Instant to, WebsiteStatus status, Platform platform, String code, String domain, Pageable pageable) {
        Organization organization = organizationRepository.findById(orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + orgId));
        
        Page<Website> page = websiteRepository.searchWebsites(organization, from, to, status, platform, code, domain, pageable);
        return mapToPageResponse(page);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<WebsiteDto> searchWebsites(UUID orgId, UUID websiteId, String code, String domain, Pageable pageable) {
        Organization organization = organizationRepository.findById(orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + orgId));
        
        Page<Website> page;
        if (websiteId != null) {
            page = websiteRepository.findByOrganizationAndId(organization, websiteId)
                    .map(w -> new org.springframework.data.domain.PageImpl<>(java.util.List.of(w), pageable, 1))
                    .orElse(new org.springframework.data.domain.PageImpl<>(java.util.List.of(), pageable, 0));
        } else if (code != null) {
            page = websiteRepository.findByOrganizationAndCode(organization, code)
                    .map(w -> new org.springframework.data.domain.PageImpl<>(java.util.List.of(w), pageable, 1))
                    .orElse(new org.springframework.data.domain.PageImpl<>(java.util.List.of(), pageable, 0));
        } else {
            page = websiteRepository.searchWebsites(organization, null, null, null, null, null, domain, pageable);
        }
        
        return mapToPageResponse(page);
    }

    @Override
    @Transactional(readOnly = true)
    public WebsiteDto getWebsiteById(UUID orgId, UUID websiteId) {
        Organization organization = organizationRepository.findById(orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + orgId));
        
        Website website = websiteRepository.findByOrganizationAndId(organization, websiteId)
                .orElseThrow(() -> new ResourceNotFoundException("Website not found with id: " + websiteId));
        return mapToDto(website);
    }

    @Override
    public WebsiteDto updateWebsite(UUID orgId, UUID websiteId, WebsiteUpdateRequest request) {
        Organization organization = organizationRepository.findById(orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + orgId));
        
        Website website = websiteRepository.findByOrganizationAndId(organization, websiteId)
                .orElseThrow(() -> new ResourceNotFoundException("Website not found with id: " + websiteId));
        
        website.setCode(request.getCode());
        website.setName(request.getName());
        website.setPlatform(request.getPlatform());
        website.setDomain(request.getDomain());
        website.setStatus(request.getStatus());
        
        Website saved = websiteRepository.save(website);
        return mapToDto(saved);
    }

    @Override
    public WebsiteDto patchWebsite(UUID orgId, UUID websiteId, WebsitePatchRequest request) {
        Organization organization = organizationRepository.findById(orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + orgId));
        
        Website website = websiteRepository.findByOrganizationAndId(organization, websiteId)
                .orElseThrow(() -> new ResourceNotFoundException("Website not found with id: " + websiteId));
        
        if (request.getCode() != null) website.setCode(request.getCode());
        if (request.getName() != null) website.setName(request.getName());
        if (request.getPlatform() != null) website.setPlatform(request.getPlatform());
        if (request.getDomain() != null) website.setDomain(request.getDomain());
        if (request.getStatus() != null) website.setStatus(request.getStatus());
        
        Website saved = websiteRepository.save(website);
        return mapToDto(saved);
    }

    @Override
    public void deleteWebsite(UUID orgId, UUID websiteId) {
        Organization organization = organizationRepository.findById(orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + orgId));
        
        Website website = websiteRepository.findByOrganizationAndId(organization, websiteId)
                .orElseThrow(() -> new ResourceNotFoundException("Website not found with id: " + websiteId));
        websiteRepository.delete(website);
    }

    private WebsiteDto mapToDto(Website website) {
        WebsiteDto dto = new WebsiteDto();
        dto.setId(website.getId());
        dto.setOrgId(website.getOrganization().getId());
        dto.setCode(website.getCode());
        dto.setName(website.getName());
        dto.setPlatform(website.getPlatform());
        dto.setDomain(website.getDomain());
        dto.setStatus(website.getStatus());
        dto.setCreatedAt(website.getCreatedAt());
        dto.setUpdatedAt(website.getUpdatedAt());
        return dto;
    }

    private PageResponse<WebsiteDto> mapToPageResponse(Page<Website> page) {
        PageResponse<WebsiteDto> response = new PageResponse<>();
        response.setData(page.getContent().stream().map(this::mapToDto).collect(Collectors.toList()));
        response.setPage(page.getNumber());
        response.setSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setHasNext(page.hasNext());
        return response;
    }
}

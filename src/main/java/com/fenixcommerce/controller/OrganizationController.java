package com.fenixcommerce.controller;

import com.fenixcommerce.dto.*;
import com.fenixcommerce.entity.OrgStatus;
import com.fenixcommerce.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @PostMapping
    public ResponseEntity<OrganizationDto> createOrganization(@Valid @RequestBody OrganizationCreateRequest request) {
        OrganizationDto response = organizationService.createOrganization(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PageResponse<OrganizationDto>> listOrganizations(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) OrgStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(defaultValue = "updatedAt,desc") String sort) {
        
        String[] sortParams = sort.split(",");
        Sort.Direction direction = sortParams.length > 1 && "asc".equalsIgnoreCase(sortParams[1]) 
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortParams[0]));
        
        PageResponse<OrganizationDto> response = organizationService.listOrganizations(from, to, name, status, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<OrganizationDto>> searchOrganizationsByRef(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<OrganizationDto> response = organizationService.listOrganizations(null, null, name, null, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationDto> getOrganizationById(@PathVariable UUID id) {
        OrganizationDto response = organizationService.getOrganizationById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizationDto> updateOrganization(
            @PathVariable UUID id,
            @Valid @RequestBody OrganizationUpdateRequest request) {
        OrganizationDto response = organizationService.updateOrganization(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<OrganizationDto> patchOrganization(
            @PathVariable UUID id,
            @Valid @RequestBody OrganizationPatchRequest request) {
        OrganizationDto response = organizationService.patchOrganization(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable UUID id) {
        organizationService.deleteOrganization(id);
        return ResponseEntity.noContent().build();
    }
}

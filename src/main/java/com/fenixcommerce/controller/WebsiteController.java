package com.fenixcommerce.controller;

import com.fenixcommerce.dto.*;
import com.fenixcommerce.entity.Platform;
import com.fenixcommerce.entity.WebsiteStatus;
import com.fenixcommerce.service.WebsiteService;
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
@RequestMapping("/organizations/{orgId}/websites")
@RequiredArgsConstructor
public class WebsiteController {

    private final WebsiteService websiteService;

    @PostMapping
    public ResponseEntity<WebsiteDto> createWebsite(
            @PathVariable UUID orgId,
            @Valid @RequestBody WebsiteCreateRequest request) {
        WebsiteDto response = websiteService.createWebsite(orgId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PageResponse<WebsiteDto>> listWebsites(
            @PathVariable UUID orgId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
            @RequestParam(required = false) WebsiteStatus status,
            @RequestParam(required = false) Platform platform,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String domain,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(defaultValue = "updatedAt,desc") String sort) {
        
        String[] sortParams = sort.split(",");
        Sort.Direction direction = sortParams.length > 1 && "asc".equalsIgnoreCase(sortParams[1]) 
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortParams[0]));
        
        PageResponse<WebsiteDto> response = websiteService.listWebsites(orgId, from, to, status, platform, code, domain, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<WebsiteDto>> searchWebsites(
            @PathVariable UUID orgId,
            @RequestParam(required = false) UUID websiteId,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String domain,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<WebsiteDto> response = websiteService.searchWebsites(orgId, websiteId, code, domain, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{websiteId}")
    public ResponseEntity<WebsiteDto> getWebsiteById(
            @PathVariable UUID orgId,
            @PathVariable UUID websiteId) {
        WebsiteDto response = websiteService.getWebsiteById(orgId, websiteId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{websiteId}")
    public ResponseEntity<WebsiteDto> updateWebsite(
            @PathVariable UUID orgId,
            @PathVariable UUID websiteId,
            @Valid @RequestBody WebsiteUpdateRequest request) {
        WebsiteDto response = websiteService.updateWebsite(orgId, websiteId, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{websiteId}")
    public ResponseEntity<WebsiteDto> patchWebsite(
            @PathVariable UUID orgId,
            @PathVariable UUID websiteId,
            @Valid @RequestBody WebsitePatchRequest request) {
        WebsiteDto response = websiteService.patchWebsite(orgId, websiteId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{websiteId}")
    public ResponseEntity<Void> deleteWebsite(
            @PathVariable UUID orgId,
            @PathVariable UUID websiteId) {
        websiteService.deleteWebsite(orgId, websiteId);
        return ResponseEntity.noContent().build();
    }
}

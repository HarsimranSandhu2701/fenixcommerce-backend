package com.fenixcommerce.service;

import com.fenixcommerce.dto.*;
import com.fenixcommerce.entity.Platform;
import com.fenixcommerce.entity.WebsiteStatus;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.UUID;

public interface WebsiteService {
    WebsiteDto createWebsite(UUID orgId, WebsiteCreateRequest request);
    PageResponse<WebsiteDto> listWebsites(UUID orgId, Instant from, Instant to, WebsiteStatus status, Platform platform, String code, String domain, Pageable pageable);
    PageResponse<WebsiteDto> searchWebsites(UUID orgId, UUID websiteId, String code, String domain, Pageable pageable);
    WebsiteDto getWebsiteById(UUID orgId, UUID websiteId);
    WebsiteDto updateWebsite(UUID orgId, UUID websiteId, WebsiteUpdateRequest request);
    WebsiteDto patchWebsite(UUID orgId, UUID websiteId, WebsitePatchRequest request);
    void deleteWebsite(UUID orgId, UUID websiteId);
}

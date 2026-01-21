package com.fenixcommerce.dto;

import com.fenixcommerce.entity.Platform;
import com.fenixcommerce.entity.WebsiteStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebsiteDto {
    private UUID id;
    private UUID orgId;
    private String code;
    private String name;
    private Platform platform;
    private String domain;
    private WebsiteStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}

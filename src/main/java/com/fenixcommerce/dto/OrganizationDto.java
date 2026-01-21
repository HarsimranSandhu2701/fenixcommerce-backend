package com.fenixcommerce.dto;

import com.fenixcommerce.entity.OrgStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDto {
    private UUID id;
    private String name;
    private OrgStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}

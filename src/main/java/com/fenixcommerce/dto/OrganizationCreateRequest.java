package com.fenixcommerce.dto;

import com.fenixcommerce.entity.OrgStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationCreateRequest {
    @NotNull(message = "Name is required")
    @Min(value = 2, message = "Name must be at least 2 characters")
    private String name;
    
    private OrgStatus status;
}

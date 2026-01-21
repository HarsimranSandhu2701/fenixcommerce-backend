package com.fenixcommerce.dto;

import com.fenixcommerce.entity.Platform;
import com.fenixcommerce.entity.WebsiteStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebsiteCreateRequest {
    @NotNull(message = "Code is required")
    @Min(value = 2, message = "Code must be at least 2 characters")
    private String code;
    
    @NotNull(message = "Name is required")
    @Min(value = 2, message = "Name must be at least 2 characters")
    private String name;
    
    @NotNull(message = "Platform is required")
    private Platform platform;
    
    private String domain;
    private WebsiteStatus status;
}

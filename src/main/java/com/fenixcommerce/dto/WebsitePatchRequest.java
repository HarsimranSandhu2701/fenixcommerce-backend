package com.fenixcommerce.dto;

import com.fenixcommerce.entity.Platform;
import com.fenixcommerce.entity.WebsiteStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebsitePatchRequest {
    private String code;
    private String name;
    private Platform platform;
    private String domain;
    private WebsiteStatus status;
}

package com.fenixcommerce.dto;

import com.fenixcommerce.entity.OrgStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationPatchRequest {
    private String name;
    private OrgStatus status;
}

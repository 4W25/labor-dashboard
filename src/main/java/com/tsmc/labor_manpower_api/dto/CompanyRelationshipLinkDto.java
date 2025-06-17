package com.tsmc.labor_manpower_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRelationshipLinkDto {
    private String source; // ID of source node
    private String target; // ID of target node
    // Can add 'value' or other properties if links have weights/styles
}

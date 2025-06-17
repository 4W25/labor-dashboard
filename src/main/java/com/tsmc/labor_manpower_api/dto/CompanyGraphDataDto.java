package com.tsmc.labor_manpower_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyGraphDataDto {
    private List<CompanyRelationshipNodeDto> nodes;
    private List<CompanyRelationshipLinkDto> links;
}

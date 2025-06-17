package com.tsmc.labor_manpower_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRelationshipNodeDto {
    private String id; // Company name or JobType name
    private Number size; // Represents number of personnel or importance
    private String group; // e.g., "main_contractor", "sub_contractor", "job_type_ME"
}

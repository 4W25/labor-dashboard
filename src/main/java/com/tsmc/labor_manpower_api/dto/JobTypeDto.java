package com.tsmc.labor_manpower_api.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobTypeDto {
    private Long jobTypeId;
    private String jobTypeName;
    private String jobTypeCode;

    // These fields are specific to when JobTypeDto is used in Personnel context (e.g., within PersonnelDetailDto)
    // They might not be populated when JobTypeDto is used for a simple list of all job types.
    private Boolean isPrimary;
    private Integer experienceYears;

    // Constructor for basic JobType info (e.g., for dropdowns)
    public JobTypeDto(Long jobTypeId, String jobTypeName, String jobTypeCode) {
        this.jobTypeId = jobTypeId;
        this.jobTypeName = jobTypeName;
        this.jobTypeCode = jobTypeCode;
        // isPrimary and experienceYears will be null for this usage
    }
}

package com.tsmc.labor_manpower_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDto {
    private long totalPersonnel;
    private long mainContractors;
    private long subContractors;
    private long totalCompanies; // Added this from service impl
    // Can add other overall stats if needed, e.g. total_certifications_expiring_soon
}

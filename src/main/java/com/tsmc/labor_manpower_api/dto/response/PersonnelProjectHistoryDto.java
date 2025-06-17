package com.tsmc.labor_manpower_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonnelProjectHistoryDto {
    private String projectName;
    private String mainContractorName;
    private String subContractorName;
    private LocalDate startDate;
    private LocalDate endDate;
    // private String role; // Role of the personnel in the project - if available
}

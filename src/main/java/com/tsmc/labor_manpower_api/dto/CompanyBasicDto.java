package com.tsmc.labor_manpower_api.dto;

import com.tsmc.labor_manpower_api.model.enums.CompanyType;
import lombok.Data;

@Data
public class CompanyBasicDto {
    private Long companyId;
    private String companyName;
    private CompanyType companyType;
    private String unifiedBusinessNumber; // Added from 4.html table
    private Long personnelCount; // Added from 4.html table (calculated)
    private java.math.BigDecimal evaluationScore; // Added from 4.html table
}

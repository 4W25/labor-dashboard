package com.tsmc.labor_manpower_api.dto;

import com.tsmc.labor_manpower_api.model.enums.CompanyType;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CompanyCreateDto {
    private String companyName;
    private CompanyType companyType;
    private String address;
    private String contactPerson;
    private String contactPhone;
    private String contactEmail;
    private String unifiedBusinessNumber;
    private BigDecimal evaluationScore;
    private BigDecimal registeredCapital;
    private String industryType;
    private String website;
    private String remarks;
    private Long parentCompanyId; // Optional, for sub-contractors
}

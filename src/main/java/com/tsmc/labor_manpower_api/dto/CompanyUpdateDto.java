package com.tsmc.labor_manpower_api.dto;

import com.tsmc.labor_manpower_api.model.enums.CompanyType;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CompanyUpdateDto {
    private String companyName;
    private CompanyType companyType; // Type might not be updatable easily
    private String address;
    private String contactPerson;
    private String contactPhone;
    private String contactEmail;
    // UnifiedBusinessNumber is typically not updatable
    private BigDecimal evaluationScore;
    private BigDecimal registeredCapital;
    private String industryType;
    private String website;
    private String remarks;
    private Long parentCompanyId;
}

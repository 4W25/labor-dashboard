package com.tsmc.labor_manpower_api.dto;

import com.tsmc.labor_manpower_api.model.enums.CertificationStatus;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CertificationDto {
    private Long personnelCertificationId; // From PersonnelCertification
    private String certificationName; // From Certification
    private String certificationNumber; // From PersonnelCertification
    private LocalDate issueDate; // From PersonnelCertification
    private LocalDate expiryDate; // From PersonnelCertification
    private CertificationStatus status; // From PersonnelCertification
    private String issuingAuthority; // From Certification
    private String scannedCopyPath; // From PersonnelCertification
}

package com.tsmc.labor_manpower_api.dto;

import com.tsmc.labor_manpower_api.model.enums.EmploymentType;
import com.tsmc.labor_manpower_api.model.enums.Gender;
import com.tsmc.labor_manpower_api.model.enums.PersonnelStatus;
import lombok.Data;
import java.time.LocalDate;

@Data
public class PersonnelCreateDto {
    private String name;
    private String workId;
    private String idNumber;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String nationality;
    private String contactNumber;
    private String email;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String department; // Not in 3.html form, but good to have
    private String jobTitle; // Not in 3.html form, but good to have
    private EmploymentType employmentType;
    private LocalDate tsmcStartDate;
    private PersonnelStatus currentStatus = PersonnelStatus.NEWCOMER; // Default for new
    private String healthGrade; // e.g. "1", "2", "3", "4"
    private String profilePhotoPath;
    private String bloodType;
    private String address;
    private String remarks;
    private Long mainCompanyId;
    private Long subCompanyId; // Optional
    private Long userId; // Optional, if linking to a system user
    // Consider adding JobType IDs and Certification IDs if creating them simultaneously
}

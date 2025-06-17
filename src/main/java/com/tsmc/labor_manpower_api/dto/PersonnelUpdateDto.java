package com.tsmc.labor_manpower_api.dto;

import com.tsmc.labor_manpower_api.model.enums.EmploymentType;
import com.tsmc.labor_manpower_api.model.enums.Gender;
import com.tsmc.labor_manpower_api.model.enums.PersonnelStatus;
import lombok.Data;
import java.time.LocalDate;

@Data
public class PersonnelUpdateDto {
    private String name;
    private String workId; // Usually not updatable, but depends on policy
    private String idNumber; // Usually not updatable
    private Gender gender;
    private LocalDate dateOfBirth;
    private String nationality;
    private String contactNumber;
    private String email;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String department;
    private String jobTitle;
    private EmploymentType employmentType;
    private LocalDate tsmcStartDate; // Usually not updatable
    private PersonnelStatus currentStatus;
    private String healthGrade;
    private String profilePhotoPath;
    private String bloodType;
    private String address;
    private String remarks;
    private Long mainCompanyId;
    private Long subCompanyId;
    // userId might not be updatable or handled differently
}

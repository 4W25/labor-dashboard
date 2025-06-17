package com.tsmc.labor_manpower_api.model.entity;

import com.tsmc.labor_manpower_api.model.enums.EmploymentType;
import com.tsmc.labor_manpower_api.model.enums.Gender;
import com.tsmc.labor_manpower_api.model.enums.PersonnelStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Personnel")
@Data
public class Personnel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PersonnelID")
    private Long personnelId;

    @Column(name = "Name", nullable = false, length = 100)
    private String name;

    @Column(name = "WorkID", unique = true, length = 50)
    private String workId;

    @Column(name = "IDNumber", unique = true, length = 50)
    private String idNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "Gender")
    private Gender gender;

    @Column(name = "DateOfBirth")
    private LocalDate dateOfBirth;

    @Column(name = "Nationality", length = 50)
    private String nationality;

    @Column(name = "ContactNumber", length = 20)
    private String contactNumber;

    @Column(name = "Email", length = 100, unique = true)
    private String email;

    @Column(name = "EmergencyContactName", length = 100)
    private String emergencyContactName;

    @Column(name = "EmergencyContactPhone", length = 20)
    private String emergencyContactPhone;

    @Column(name = "Department", length = 100)
    private String department;

    @Column(name = "JobTitle", length = 100)
    private String jobTitle;

    @Enumerated(EnumType.STRING)
    @Column(name = "EmploymentType")
    private EmploymentType employmentType;

    @Column(name = "TSMCStartDate")
    private LocalDate tsmcStartDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "CurrentStatus", nullable = false)
    private PersonnelStatus currentStatus;

    @Column(name = "HealthGrade", length = 10)
    private String healthGrade;

    @Column(name = "SafetyRecordSummary", columnDefinition = "TEXT")
    private String safetyRecordSummary;

    @Column(name = "ProfilePhotoPath", length = 255)
    private String profilePhotoPath;

    @Column(name = "BloodType", length = 10)
    private String bloodType;

    @Column(name = "Address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "Remarks", columnDefinition = "TEXT")
    private String remarks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MainCompanyID", nullable = false)
    private Company mainCompany;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SubCompanyID")
    private Company subCompany;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", unique = true)
    private User user;

    @CreationTimestamp
    @Column(name = "CreatedAt", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;
}

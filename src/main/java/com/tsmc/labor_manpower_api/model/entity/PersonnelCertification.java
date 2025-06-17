package com.tsmc.labor_manpower_api.model.entity;

import com.tsmc.labor_manpower_api.model.enums.CertificationStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "PersonnelCertifications")
@Data
public class PersonnelCertification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PersonnelCertificationID")
    private Long personnelCertificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PersonnelID", nullable = false)
    private Personnel personnel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CertificationID", nullable = false)
    private Certification certification;

    @Column(name = "CertificationNumber", length = 100)
    private String certificationNumber;

    @Column(name = "IssueDate")
    private LocalDate issueDate;

    @Column(name = "ExpiryDate")
    private LocalDate expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", nullable = false)
    private CertificationStatus status;

    @Column(name = "ScannedCopyPath", length = 255)
    private String scannedCopyPath;

    @Column(name = "Remarks", columnDefinition = "TEXT")
    private String remarks;

    @CreationTimestamp
    @Column(name = "CreatedAt", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;
}

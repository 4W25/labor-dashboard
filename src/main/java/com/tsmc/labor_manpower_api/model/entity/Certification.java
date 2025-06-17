package com.tsmc.labor_manpower_api.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Certifications")
@Data
public class Certification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CertificationID")
    private Long certificationId;

    @Column(name = "CertificationName", nullable = false, length = 255)
    private String certificationName;

    @Column(name = "IssuingAuthority", length = 255)
    private String issuingAuthority;

    @Column(name = "StandardValidityPeriodMonths")
    private Integer standardValidityPeriodMonths;

    @Column(name = "Description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "JobTypeID")
    private JobType jobType;

    @CreationTimestamp
    @Column(name = "CreatedAt", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;
}

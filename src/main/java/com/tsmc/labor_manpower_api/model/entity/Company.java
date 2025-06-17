package com.tsmc.labor_manpower_api.model.entity;

import com.tsmc.labor_manpower_api.model.enums.CompanyType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "Companies")
@Data
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CompanyID")
    private Long companyId;

    @Column(name = "CompanyName", nullable = false, length = 255)
    private String companyName;

    @Enumerated(EnumType.STRING)
    @Column(name = "CompanyType", nullable = false)
    private CompanyType companyType;

    @Column(name = "Address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "ContactPerson", length = 100)
    private String contactPerson;

    @Column(name = "ContactPhone", length = 20)
    private String contactPhone;

    @Column(name = "ContactEmail", length = 100)
    private String contactEmail;

    @Column(name = "UnifiedBusinessNumber", length = 20, unique = true)
    private String unifiedBusinessNumber;

    @Column(name = "EvaluationScore", precision = 5, scale = 2)
    private BigDecimal evaluationScore;

    @Column(name = "RegisteredCapital", precision = 15, scale = 2)
    private BigDecimal registeredCapital;

    @Column(name = "IndustryType", length = 100)
    private String industryType;

    @Column(name = "Website", length = 255)
    private String website;

    @Column(name = "Remarks", columnDefinition = "TEXT")
    private String remarks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ParentCompanyID")
    private Company parentCompany;

    @OneToMany(mappedBy = "parentCompany")
    private Set<Company> subCompanies;

    @CreationTimestamp
    @Column(name = "CreatedAt", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;
}

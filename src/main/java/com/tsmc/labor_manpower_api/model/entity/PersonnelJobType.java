package com.tsmc.labor_manpower_api.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "PersonnelJobTypes")
@Data
public class PersonnelJobType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PersonnelJobTypeID")
    private Long personnelJobTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PersonnelID", nullable = false)
    private Personnel personnel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "JobTypeID", nullable = false)
    private JobType jobType;

    @Column(name = "IsPrimary", nullable = false)
    private boolean isPrimary;

    @Column(name = "ExperienceYears")
    private Integer experienceYears;

    @CreationTimestamp
    @Column(name = "CreatedAt", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;
}

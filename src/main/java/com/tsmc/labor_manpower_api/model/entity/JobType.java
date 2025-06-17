package com.tsmc.labor_manpower_api.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "JobTypes")
@Data
public class JobType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "JobTypeID")
    private Long jobTypeId;

    @Column(name = "JobTypeCode", nullable = false, unique = true, length = 50)
    private String jobTypeCode;

    @Column(name = "JobTypeName", nullable = false, length = 100)
    private String jobTypeName;

    @Column(name = "Description", columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    @Column(name = "CreatedAt", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;
}

package com.tsmc.labor_manpower_api.model.entity;

import com.tsmc.labor_manpower_api.model.enums.IncidentStatus;
import com.tsmc.labor_manpower_api.model.enums.SeverityLevel;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "SafetyIncidents")
@Data
public class SafetyIncident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IncidentID")
    private Long incidentId;

    @Column(name = "IncidentDate", nullable = false)
    private LocalDateTime incidentDate;

    @Column(name = "ReportedDate", nullable = false)
    private LocalDateTime reportedDate;

    @Column(name = "Location", length = 255)
    private String location;

    @Column(name = "IncidentType", length = 100)
    private String incidentType;

    @Column(name = "Description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "SeverityLevel", nullable = false)
    private SeverityLevel severityLevel;

    @Column(name = "ActionTaken", columnDefinition = "TEXT")
    private String actionTaken;

    @Column(name = "Witnesses", columnDefinition = "TEXT")
    private String witnesses;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", nullable = false)
    private IncidentStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PersonnelID")
    private Personnel personnel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CompanyID")
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ReportedByUserID")
    private User reportedByUser;

    @Column(name = "ProjectID")
    private Integer projectId;

    @CreationTimestamp
    @Column(name = "CreatedAt", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;
}

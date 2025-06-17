package com.tsmc.labor_manpower_api.dto;

import com.tsmc.labor_manpower_api.model.enums.IncidentStatus;
import com.tsmc.labor_manpower_api.model.enums.SeverityLevel;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SafetyIncidentDto { // Representing a SafetyIncident related to a Personnel
    private Long incidentId;
    private LocalDateTime incidentDate;
    private String location;
    private String incidentType;
    private String description;
    private SeverityLevel severityLevel;
    private IncidentStatus status;
    private String companyName; // Company involved in the incident
}

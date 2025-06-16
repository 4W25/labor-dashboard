CREATE TABLE SafetyIncidents (
    IncidentID INT PRIMARY KEY AUTO_INCREMENT,
    PersonnelID INT NOT NULL,
    ProjectID INT,
    CompanyID INT,
    IncidentDate DATETIME NOT NULL,
    ReportedDate DATETIME,
    Location VARCHAR(255),
    IncidentType VARCHAR(100) NOT NULL,
    Description TEXT NOT NULL,
    SeverityLevel ENUM('Minor', 'Moderate', 'Major', 'Critical') NOT NULL,
    ActionTaken TEXT,
    Witnesses TEXT,
    ReportedByUserID INT,
    Status ENUM('Open', 'UnderInvestigation', 'Closed') DEFAULT 'Open',
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

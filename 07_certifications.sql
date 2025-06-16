CREATE TABLE Certifications (
    CertificationID INT PRIMARY KEY AUTO_INCREMENT,
    CertificationName VARCHAR(255) UNIQUE NOT NULL,
    IssuingAuthority VARCHAR(255),
    StandardValidityPeriodMonths INT,
    Description TEXT,
    JobTypeID INT,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

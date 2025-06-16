CREATE TABLE PersonnelCertifications (
    PersonnelCertificationID INT PRIMARY KEY AUTO_INCREMENT,
    PersonnelID INT NOT NULL,
    CertificationID INT NOT NULL,
    CertificationNumber VARCHAR(100),
    IssueDate DATE NOT NULL,
    ExpiryDate DATE NOT NULL,
    Status ENUM('Valid', 'Expired', 'ExpiringSoon') NOT NULL,
    ScannedCopyPath VARCHAR(255),
    Remarks TEXT,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uq_personnel_certification_issuedate (PersonnelID, CertificationID, IssueDate)
);

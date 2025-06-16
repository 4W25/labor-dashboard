CREATE TABLE Companies (
    CompanyID INT PRIMARY KEY AUTO_INCREMENT,
    CompanyName VARCHAR(255) NOT NULL,
    CompanyType ENUM('MainContractor', 'SubContractor', 'Supplier') NOT NULL,
    ParentCompanyID INT,
    Address TEXT,
    ContactPerson VARCHAR(255),
    ContactPhone VARCHAR(50),
    ContactEmail VARCHAR(255),
    UnifiedBusinessNumber VARCHAR(20) UNIQUE,
    EvaluationScore DECIMAL(5,2),
    RegisteredCapital DECIMAL(20,2),
    IndustryType VARCHAR(100),
    Website VARCHAR(255),
    Remarks TEXT,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

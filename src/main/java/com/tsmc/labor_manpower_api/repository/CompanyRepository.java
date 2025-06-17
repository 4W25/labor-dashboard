package com.tsmc.labor_manpower_api.repository;

import com.tsmc.labor_manpower_api.model.entity.Company;
import com.tsmc.labor_manpower_api.model.enums.CompanyType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByCompanyName(String companyName);
    Optional<Company> findByUnifiedBusinessNumber(String unifiedBusinessNumber);
    List<Company> findByCompanyType(CompanyType companyType);
    long countByCompanyType(CompanyType companyType); // Added for dashboard stats
    List<Company> findByParentCompanyCompanyId(Long parentCompanyId);
    boolean existsByUnifiedBusinessNumber(String unifiedBusinessNumber); // Added for CompanyService validation
    boolean existsByParentCompanyCompanyId(Long parentCompanyId); // Added for CompanyService validation
}

package com.tsmc.labor_manpower_api.repository;

import com.tsmc.labor_manpower_api.model.entity.Personnel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;

public interface PersonnelRepository extends JpaRepository<Personnel, Long>, JpaSpecificationExecutor<Personnel> {
    Optional<Personnel> findByWorkId(String workId);
    Optional<Personnel> findByIdNumber(String idNumber);
    Optional<Personnel> findByEmail(String email);

    long countByMainCompanyCompanyIdOrSubCompanyCompanyId(Long mainCompanyId, Long subCompanyId);

    List<Personnel> findByMainCompanyCompanyIdOrSubCompanyCompanyId(Long mainCompanyId, Long subCompanyId); // For CompanyDashboardServiceImpl stats

    // For fetching top N personnel for CompanyDetailDto
    // Spring Data JPA doesn't directly support "findTopN" with multiple criteria like this easily.
    // A custom query or fetching more and limiting in service might be needed.
    // For simplicity, using a method that fetches all and letting service limit, or using a query.
    // Let's define it to fetch all for now, service will limit.
    // List<Personnel> findByMainCompany_CompanyIdOrSubCompany_CompanyId(Long mainCompanyId, Long subCompanyId, Pageable pageable);
    // For findTopN, a query is better:
    @Query("SELECT p FROM Personnel p WHERE p.mainCompany.companyId = :companyId OR p.subCompany.companyId = :companyId ORDER BY p.tsmcStartDate DESC") // Example order
    List<Personnel> findTopNByMainCompanyCompanyIdOrSubCompanyCompanyId(@Param("companyId") Long companyId, org.springframework.data.domain.Pageable pageable);

    List<Personnel> findBySubCompanyCompanyId(Long subCompanyId); // For CompanyDashboardServiceImpl relationships

    long countByMainCompanyCompanyIdOrSubCompanyCompanyIdAndHealthGradeIn(Long mainCompanyId, Long subCompanyId, List<String> healthGrades);


    // Add more custom query methods if needed for filtering later
}

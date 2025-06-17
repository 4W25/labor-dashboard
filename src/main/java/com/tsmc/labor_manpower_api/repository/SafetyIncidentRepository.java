package com.tsmc.labor_manpower_api.repository;

import com.tsmc.labor_manpower_api.model.entity.SafetyIncident;
import com.tsmc.labor_manpower_api.model.enums.IncidentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.time.LocalDateTime;

public interface SafetyIncidentRepository extends JpaRepository<SafetyIncident, Long> {
    List<SafetyIncident> findByPersonnelPersonnelId(Long personnelId);
    List<SafetyIncident> findByCompanyCompanyId(Long companyId);
    List<SafetyIncident> findByIncidentDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<SafetyIncident> findByStatus(IncidentStatus status);
    List<SafetyIncident> findByPersonnelPersonnelIdAndIncidentDateBetween(Long personnelId, LocalDateTime startDate, LocalDateTime endDate);

    // For CompanyServiceImpl (CompanyDetailDto)
    List<SafetyIncident> findTopNByCompanyCompanyIdOrderByIncidentDateDesc(Long companyId, org.springframework.data.domain.Pageable pageable);

    // For CompanyDashboardServiceImpl
    List<SafetyIncident> findByCompanyCompanyIdAndIncidentDateAfter(Long companyId, LocalDateTime date);

    long countByPersonnelPersonnelId(Long personnelId); // Added for PersonnelDetailDto

    @Query("SELECT COUNT(DISTINCT si.personnel.personnelId) FROM SafetyIncident si WHERE si.company.companyId = :companyId")
    long countDistinctPersonnelWithIncidentsByCompanyId(@Param("companyId") Long companyId);
}

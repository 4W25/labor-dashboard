package com.tsmc.labor_manpower_api.service;

import com.tsmc.labor_manpower_api.dto.CompanyPageStatsDto; // Added
import com.tsmc.labor_manpower_api.dto.ProjectHistoryDto; // Added
import com.tsmc.labor_manpower_api.dto.chart.ChartDataDto; // Added
import com.tsmc.labor_manpower_api.dto.CompanyGraphDataDto; // Added

import java.util.Map;
import java.util.List;

public interface CompanyDashboardService {
    CompanyPageStatsDto getCompanyStats(Long companyId); // Changed return type
    CompanyPageStatsDto getCompanyStatsForDetail(Long companyId); // New method for CompanyServiceImpl
    ChartDataDto getExperienceDistribution(Long companyId); // Changed return type
    ChartDataDto getViolationHistory(Long companyId);     // Changed return type
    ChartDataDto getJobTypeDistribution(Long companyId);   // Changed return type
    ChartDataDto getHealthRiskDistribution(Long companyId); // Changed return type
    List<ProjectHistoryDto> getProjectHistory(Long companyId);  // Changed return type
    CompanyGraphDataDto getCompanyRelationshipData(); // Changed return type
}

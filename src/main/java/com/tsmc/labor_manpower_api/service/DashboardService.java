package com.tsmc.labor_manpower_api.service;

// Import DTOs for stats and charts later
// import com.tsmc.labor_manpower_api.dto.DashboardStatsDto;
// import com.tsmc.labor_manpower_api.dto.ChartDataDto;
import java.util.List; // Added for chart data values
import java.util.Map; // Temporary for chart data

public interface DashboardService {
    // DashboardStatsDto getOverallStats(); // Placeholder
    Map<String, Long> getOverallStats(); // Simplified
    Map<String, Object> getJobDistributionChartData(); // Placeholder for ChartDataDto
    Map<String, Object> getAgeDistributionChartData(); // Placeholder for ChartDataDto
}

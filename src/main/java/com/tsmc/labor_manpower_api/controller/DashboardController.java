package com.tsmc.labor_manpower_api.controller;

import com.tsmc.labor_manpower_api.service.DashboardService;
// Import DTOs for chart data
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getOverallStats() {
        // Replace Map with a specific DashboardStatsDto later
        return ResponseEntity.ok(dashboardService.getOverallStats());
    }

    @GetMapping("/charts/job-distribution")
    public ResponseEntity<Map<String, Object>> getJobDistributionChart() {
        // Replace Map with a specific ChartDataDto later
        return ResponseEntity.ok(dashboardService.getJobDistributionChartData());
    }

    @GetMapping("/charts/age-distribution")
    public ResponseEntity<Map<String, Object>> getAgeDistributionChart() {
        // Replace Map with a specific ChartDataDto later
        return ResponseEntity.ok(dashboardService.getAgeDistributionChartData());
    }
}

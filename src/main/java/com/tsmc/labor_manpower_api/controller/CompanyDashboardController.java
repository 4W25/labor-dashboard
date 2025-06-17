package com.tsmc.labor_manpower_api.controller;

import com.tsmc.labor_manpower_api.service.CompanyDashboardService;
// Import DTOs
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/companies") // Base path, specific endpoints below
public class CompanyDashboardController {

    private final CompanyDashboardService companyDashboardService;

    @Autowired
    public CompanyDashboardController(CompanyDashboardService companyDashboardService) {
        this.companyDashboardService = companyDashboardService;
    }

    // For 4.html - Company Info Page
    @GetMapping("/{companyId}/stats")
    public ResponseEntity<Map<String, Object>> getCompanyStats(@PathVariable Long companyId) {
        // Replace Map with CompanyStatsDto
        try {
            return ResponseEntity.ok(companyDashboardService.getCompanyStats(companyId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // If company not found
        }
    }

    @GetMapping("/{companyId}/charts/experience-distribution")
    public ResponseEntity<Map<String, Object>> getExperienceDistribution(@PathVariable Long companyId) {
        // Replace Map with ChartDataDto
        try {
            return ResponseEntity.ok(companyDashboardService.getExperienceDistribution(companyId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{companyId}/charts/violation-history")
    public ResponseEntity<Map<String, Object>> getViolationHistory(@PathVariable Long companyId) {
        // Replace Map with ChartDataDto
        try {
            return ResponseEntity.ok(companyDashboardService.getViolationHistory(companyId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{companyId}/charts/jobtype-distribution")
    public ResponseEntity<Map<String, Object>> getJobTypeDistribution(@PathVariable Long companyId) {
        // Replace Map with ChartDataDto
        try {
            return ResponseEntity.ok(companyDashboardService.getJobTypeDistribution(companyId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{companyId}/charts/health-risk-distribution")
    public ResponseEntity<Map<String, Object>> getHealthRiskDistribution(@PathVariable Long companyId) {
        // Replace Map with ChartDataDto
        try {
            return ResponseEntity.ok(companyDashboardService.getHealthRiskDistribution(companyId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{companyId}/projects")
    public ResponseEntity<List<Map<String, Object>>> getProjectHistory(@PathVariable Long companyId) {
        // Replace List<Map> with List<ProjectHistoryDto>
        try {
            return ResponseEntity.ok(companyDashboardService.getProjectHistory(companyId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // For 5.html - Company Relationship Page
    @GetMapping("/relationships")
    public ResponseEntity<Map<String, Object>> getCompanyRelationshipData() {
        // Replace Map with a specific GraphDataDto
        // This endpoint doesn't depend on a companyId path variable, so less likely to throw a "not found" for a specific company.
        // However, the underlying service logic might encounter issues if data is inconsistent.
        return ResponseEntity.ok(companyDashboardService.getCompanyRelationshipData());
    }
}

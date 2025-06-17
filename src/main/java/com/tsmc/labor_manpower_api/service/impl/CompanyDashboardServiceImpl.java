package com.tsmc.labor_manpower_api.service.impl;

import com.tsmc.labor_manpower_api.dto.*;
import com.tsmc.labor_manpower_api.dto.chart.ChartDataDto;
import com.tsmc.labor_manpower_api.dto.chart.ChartDatasetDto;
import com.tsmc.labor_manpower_api.mapper.DashboardMapper;
import com.tsmc.labor_manpower_api.model.entity.*;
import com.tsmc.labor_manpower_api.model.enums.CompanyType;
import com.tsmc.labor_manpower_api.repository.*;
import com.tsmc.labor_manpower_api.service.CompanyDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Objects;


@Service
@Transactional(readOnly = true)
public class CompanyDashboardServiceImpl implements CompanyDashboardService {

    private final CompanyRepository companyRepository;
    private final PersonnelRepository personnelRepository;
    private final SafetyIncidentRepository safetyIncidentRepository;
    private final PersonnelJobTypeRepository personnelJobTypeRepository;
    private final JobTypeRepository jobTypeRepository;

    @Autowired
    public CompanyDashboardServiceImpl(CompanyRepository companyRepository,
                                     PersonnelRepository personnelRepository,
                                     SafetyIncidentRepository safetyIncidentRepository,
                                     PersonnelJobTypeRepository personnelJobTypeRepository,
                                     JobTypeRepository jobTypeRepository) {
        this.companyRepository = companyRepository;
        this.personnelRepository = personnelRepository;
        this.safetyIncidentRepository = safetyIncidentRepository;
        this.personnelJobTypeRepository = personnelJobTypeRepository;
        this.jobTypeRepository = jobTypeRepository;
    }

    public CompanyPageStatsDto getCompanyStatsForDetail(Long companyId) {
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found: " + companyId));

        long personnelCount = personnelRepository.countByMainCompanyCompanyIdOrSubCompanyCompanyId(companyId, companyId);

        // Define high-risk health grades, e.g. "3" and "4"
        List<String> highRiskGrades = List.of("3", "4", "5"); // Assuming 5 is also high risk
        long highRiskPersonnelCount = 0;
        if (personnelCount > 0) { // Only query if there are personnel
             highRiskPersonnelCount = personnelRepository.countByMainCompanyCompanyIdOrSubCompanyCompanyIdAndHealthGradeIn(companyId, companyId, highRiskGrades);
        }

        long distinctPersonnelWithIncidents = 0;
        if (personnelCount > 0) { // Only query if there are personnel
            distinctPersonnelWithIncidents = safetyIncidentRepository.countDistinctPersonnelWithIncidentsByCompanyId(companyId);
        }
        double violationPercentage = personnelCount > 0 ? (double) distinctPersonnelWithIncidents / personnelCount * 100 : 0;

        CompanyPageStatsDto statsDto = new CompanyPageStatsDto();
        statsDto.setPersonnelCount(personnelCount);
        statsDto.setMaxAttendance(personnelCount); // Placeholder - needs actual attendance data
        statsDto.setAvgAttendance((long)(personnelCount * 0.8)); // Placeholder - needs actual attendance data
        statsDto.setEvaluationScore(company.getEvaluationScore() != null ? company.getEvaluationScore() : BigDecimal.ZERO);
        statsDto.setHighRiskPersonnelCount(highRiskPersonnelCount);
        statsDto.setViolationPersonnelPercentage(String.format("%.1f%%", violationPercentage));
        return statsDto;
    }

    @Override
    public CompanyPageStatsDto getCompanyStats(Long companyId) {
       return getCompanyStatsForDetail(companyId);
    }

    @Override
    public ChartDataDto getExperienceDistribution(Long companyId) {
        List<Personnel> companyPersonnel = personnelRepository.findByMainCompanyCompanyIdOrSubCompanyCompanyId(companyId, companyId);
        Map<String, Long> experienceGroups = companyPersonnel.stream()
            .filter(p -> p.getTsmcStartDate() != null)
            .map(p -> Period.between(p.getTsmcStartDate(), LocalDate.now()).toTotalMonths())
            .collect(Collectors.groupingBy(totalMonths -> {
                if (totalMonths < 12) return "<1年";
                if (totalMonths < 36) return "1-3年";
                if (totalMonths < 60) return "3-5年";
                return "5年以上";
            }, Collectors.counting()));

        List<String> labels = List.of("<1年", "1-3年", "3-5年", "5年以上");
        List<Long> values = labels.stream()
            .map(label -> experienceGroups.getOrDefault(label, 0L))
            .collect(Collectors.toList());

        return new ChartDataDto(labels, List.of(new ChartDatasetDto("年資分布", values, null)));
    }

    @Override
    public ChartDataDto getViolationHistory(Long companyId) {
        LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        // The following query needs a DTO/Projection: MonthlyCountProjection
        // List<MonthlyCountProjection> monthlyCounts = safetyIncidentRepository.findMonthlyIncidentCountsByCompanyId(companyId, oneYearAgo);

        // Manual aggregation if the query isn't added or doesn't work:
        List<SafetyIncident> incidents = safetyIncidentRepository.findByCompanyCompanyIdAndIncidentDateAfter(companyId, oneYearAgo);
        Map<String, Long> monthlyViolations = incidents.stream()
             .collect(Collectors.groupingBy(incident -> incident.getIncidentDate().format(DateTimeFormatter.ofPattern("yyyy/MM")),
                                            Collectors.counting()));

        List<String> labels = new ArrayList<>();
        LocalDate currentMonth = LocalDate.now().withDayOfMonth(1);
        for (int i = 0; i < 12; i++) {
            labels.add(0, currentMonth.minusMonths(i).format(DateTimeFormatter.ofPattern("yyyy/MM")));
        }

        List<Long> values = labels.stream()
            .map(label -> monthlyViolations.getOrDefault(label, 0L))
            .collect(Collectors.toList());

        return new ChartDataDto(labels, List.of(new ChartDatasetDto("近一年違規次數", values, null)));
    }

    @Override
    public ChartDataDto getJobTypeDistribution(Long companyId) {
        List<String> monthLabels = new ArrayList<>();
        LocalDate currentMonth = LocalDate.now().withDayOfMonth(1);
        for (int i = 0; i < 12; i++) {
            monthLabels.add(0, currentMonth.minusMonths(i).format(DateTimeFormatter.ofPattern("yyyy/MM")));
        }

        List<Personnel> companyPersonnel = personnelRepository.findByMainCompanyCompanyIdOrSubCompanyCompanyId(companyId, companyId);
        List<Long> personnelIds = companyPersonnel.stream().map(Personnel::getPersonnelId).collect(Collectors.toList());

        List<ChartDatasetDto> datasets = new ArrayList<>();
        if (!personnelIds.isEmpty()) {
            Map<String, Long> currentJobTypeCounts = personnelJobTypeRepository.findByPersonnelPersonnelIdIn(personnelIds).stream()
                .filter(pjt -> pjt.getJobType() != null)
                .collect(Collectors.groupingBy(pjt -> pjt.getJobType().getJobTypeName(), Collectors.counting()));

            jobTypeRepository.findAll().forEach(jt -> {
                long count = currentJobTypeCounts.getOrDefault(jt.getJobTypeName(), 0L);
                if (count > 0) {
                    datasets.add(new ChartDatasetDto(
                        jt.getJobTypeName(),
                        monthLabels.stream().map(m -> count).collect(Collectors.toList()),
                        null
                    ));
                }
            });
        }
        return new ChartDataDto(monthLabels, datasets);
    }

    @Override
    public ChartDataDto getHealthRiskDistribution(Long companyId) {
        List<String> monthLabels = new ArrayList<>();
        LocalDate currentMonth = LocalDate.now().withDayOfMonth(1);
        for (int i = 0; i < 12; i++) {
            monthLabels.add(0, currentMonth.minusMonths(i).format(DateTimeFormatter.ofPattern("yyyy/MM")));
        }

        List<Personnel> companyPersonnel = personnelRepository.findByMainCompanyCompanyIdOrSubCompanyCompanyId(companyId, companyId);
        Map<String, Long> currentHealthGradeCounts = companyPersonnel.stream()
            .filter(p -> p.getHealthGrade() != null && !p.getHealthGrade().isEmpty())
            .collect(Collectors.groupingBy(Personnel::getHealthGrade, Collectors.counting()));

        List<ChartDatasetDto> datasets = new ArrayList<>();
        List<String> healthGradeLevels = List.of("1", "2", "3", "4");
        healthGradeLevels.forEach(hg -> {
            long count = currentHealthGradeCounts.getOrDefault(hg, 0L);
            datasets.add(new ChartDatasetDto(
                "Level " + hg,
                monthLabels.stream().map(m -> count).collect(Collectors.toList()),
                null
            ));
        });
        return new ChartDataDto(monthLabels, datasets);
    }

    @Override
    public List<ProjectHistoryDto> getProjectHistory(Long companyId) {
        return List.of(
            new ProjectHistoryDto("南科晶圓擴建", "2022/03 - 2022/12", 80, "已完成"),
            new ProjectHistoryDto("竹科新廠建置", "2023/01 - 2023/11", 95, "已完成"),
            new ProjectHistoryDto("中科擴廠工程", "2024/02 - Ongoing", 110, "進行中")
        );
    }

    @Override
    public CompanyGraphDataDto getCompanyRelationshipData() {
        List<CompanyRelationshipNodeDto> nodes = new ArrayList<>();
        List<CompanyRelationshipLinkDto> links = new ArrayList<>();
        List<Company> allCompanies = companyRepository.findAll();

        for (Company c : allCompanies) {
            long personnelInCompany = personnelRepository.countByMainCompanyCompanyIdOrSubCompanyCompanyId(c.getCompanyId(), c.getCompanyId());
            nodes.add(new CompanyRelationshipNodeDto(
                c.getCompanyName(),
                Math.max(10, personnelInCompany),
                c.getCompanyType() == CompanyType.MAIN_CONTRACTOR ? "main" : (c.getCompanyType() == CompanyType.SUB_CONTRACTOR ? "sub" : "supplier")
            ));
            if (c.getParentCompany() != null) {
                 links.add(new CompanyRelationshipLinkDto(c.getParentCompany().getCompanyName(), c.getCompanyName()));
            }
        }

        Map<String, Long> jobTypeCountsOverall = personnelJobTypeRepository.findAll().stream()
            .filter(pjt -> pjt.getJobType() != null)
            .collect(Collectors.groupingBy(pjt -> pjt.getJobType().getJobTypeName(), Collectors.counting()));

        jobTypeCountsOverall.forEach((jobTypeName, count) -> {
            nodes.add(new CompanyRelationshipNodeDto(jobTypeName, Math.max(5, count), "job_type_" + jobTypeName.toLowerCase()));
        });

        allCompanies.stream().filter(c -> c.getCompanyType() == CompanyType.SUB_CONTRACTOR).forEach(subCompany -> {
            List<Long> personnelIdsInSubCompany = personnelRepository.findBySubCompanyCompanyId(subCompany.getCompanyId())
                .stream().map(Personnel::getPersonnelId).collect(Collectors.toList());

            if (!personnelIdsInSubCompany.isEmpty()) {
                personnelJobTypeRepository.findByPersonnelPersonnelIdIn(personnelIdsInSubCompany).stream()
                    .filter(pjt -> pjt.getJobType() != null)
                    .collect(Collectors.groupingBy(pjt -> pjt.getJobType().getJobTypeName(), Collectors.counting()))
                    .forEach((jobTypeName, count) -> {
                         if (count > 0) links.add(new CompanyRelationshipLinkDto(subCompany.getCompanyName(), jobTypeName));
                    });
            }
        });
        return new CompanyGraphDataDto(nodes, links);
    }
}

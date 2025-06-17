package com.tsmc.labor_manpower_api.service.impl;

import com.tsmc.labor_manpower_api.model.entity.JobType;
import com.tsmc.labor_manpower_api.model.entity.PersonnelJobType;
import com.tsmc.labor_manpower_api.repository.CompanyRepository;
import com.tsmc.labor_manpower_api.repository.PersonnelRepository;
import com.tsmc.labor_manpower_api.repository.JobTypeRepository;
import com.tsmc.labor_manpower_api.repository.PersonnelJobTypeRepository;
import com.tsmc.labor_manpower_api.model.enums.CompanyType;
import com.tsmc.labor_manpower_api.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.Period;


@Service
public class DashboardServiceImpl implements DashboardService {

    private final PersonnelRepository personnelRepository;
    private final CompanyRepository companyRepository;
    private final JobTypeRepository jobTypeRepository; // Added this
    private final PersonnelJobTypeRepository personnelJobTypeRepository;


    @Autowired
    public DashboardServiceImpl(PersonnelRepository personnelRepository,
                               CompanyRepository companyRepository,
                               JobTypeRepository jobTypeRepository, // Added this
                               PersonnelJobTypeRepository personnelJobTypeRepository) {
        this.personnelRepository = personnelRepository;
        this.companyRepository = companyRepository;
        this.jobTypeRepository = jobTypeRepository; // Added this
        this.personnelJobTypeRepository = personnelJobTypeRepository;
    }

    @Override
    public Map<String, Long> getOverallStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalPersonnel", personnelRepository.count());
        stats.put("mainContractors", companyRepository.countByCompanyType(CompanyType.MAIN_CONTRACTOR));
        stats.put("subContractors", companyRepository.countByCompanyType(CompanyType.SUB_CONTRACTOR));
        stats.put("totalCompanies", companyRepository.count());
        return stats;
    }

    @Override
    public Map<String, Object> getJobDistributionChartData() {
        List<com.tsmc.labor_manpower_api.repository.projection.JobTypeDistributionProjection> distribution = personnelJobTypeRepository.findJobTypeDistribution();

        List<String> labels = distribution.stream()
                                          .map(com.tsmc.labor_manpower_api.repository.projection.JobTypeDistributionProjection::getJobTypeName)
                                          .collect(Collectors.toList());
        List<Long> values = distribution.stream()
                                        .map(com.tsmc.labor_manpower_api.repository.projection.JobTypeDistributionProjection::getCount)
                                        .collect(Collectors.toList());

        Map<String, Object> data = new HashMap<>();
        data.put("labels", labels);
        data.put("values", values);
        data.put("datasetLabel", "Job Type Distribution");
        return data;
    }

    @Override
    public Map<String, Object> getAgeDistributionChartData() {
        Map<String, Long> ageGroups = personnelRepository.findAll().stream()
            .filter(p -> p.getDateOfBirth() != null)
            .map(p -> Period.between(p.getDateOfBirth(), LocalDate.now()).getYears())
            .collect(Collectors.groupingBy(age -> {
                if (age < 20) return "<20";
                if (age <= 29) return "20-29";
                if (age <= 39) return "30-39";
                if (age <= 49) return "40-49";
                if (age <= 59) return "50-59";
                return "60+";
            }, Collectors.counting()));

        List<String> ageLabels = List.of("<20", "20-29", "30-39", "40-49", "50-59", "60+");
        List<Long> ageValues = ageLabels.stream()
                                    .map(label -> ageGroups.getOrDefault(label, 0L))
                                    .collect(Collectors.toList());

        Map<String, Object> data = new HashMap<>();
        data.put("labels", ageLabels);
        data.put("values", ageValues);
        data.put("datasetLabel", "Age Distribution"); // Added for DashboardMapper
        return data;
    }
}

package com.tsmc.labor_manpower_api.service.impl;

import com.tsmc.labor_manpower_api.dto.*;
import com.tsmc.labor_manpower_api.mapper.CompanyMapper;
import com.tsmc.labor_manpower_api.mapper.PersonnelMapper;
import com.tsmc.labor_manpower_api.model.entity.Company;
import com.tsmc.labor_manpower_api.model.entity.Personnel;
import com.tsmc.labor_manpower_api.model.entity.SafetyIncident;
import com.tsmc.labor_manpower_api.model.enums.CompanyType;
import com.tsmc.labor_manpower_api.repository.CompanyRepository;
import com.tsmc.labor_manpower_api.repository.PersonnelRepository;
import com.tsmc.labor_manpower_api.repository.SafetyIncidentRepository;
import com.tsmc.labor_manpower_api.repository.PersonnelJobTypeRepository; // For primary job type for personnel list
import com.tsmc.labor_manpower_api.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.criteria.Predicate;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final PersonnelRepository personnelRepository;
    private final SafetyIncidentRepository safetyIncidentRepository;
    private final PersonnelJobTypeRepository personnelJobTypeRepository;
    private final CompanyDashboardServiceImpl companyDashboardService; // To get stats for detail view

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository,
                              PersonnelRepository personnelRepository,
                              SafetyIncidentRepository safetyIncidentRepository,
                              PersonnelJobTypeRepository personnelJobTypeRepository,
                              CompanyDashboardServiceImpl companyDashboardService) {
        this.companyRepository = companyRepository;
        this.personnelRepository = personnelRepository;
        this.safetyIncidentRepository = safetyIncidentRepository;
        this.personnelJobTypeRepository = personnelJobTypeRepository;
        this.companyDashboardService = companyDashboardService;
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyDetailDto getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Company not found with id: " + id));

        // Fetching stats for the company detail page
        CompanyPageStatsDto statsDto = companyDashboardService.getCompanyStatsForDetail(id);


        // Fetch a limited list of key personnel
        List<Personnel> keyPersonnelEntities = personnelRepository.findTopNByMainCompanyCompanyIdOrSubCompanyCompanyId(id, id, PageRequest.of(0, 10)); // Example: top 10
        List<CompanyPersonnelDto> keyPersonnelDtos = keyPersonnelEntities.stream().map(p -> {
            String primaryJob = personnelJobTypeRepository.findByPersonnelPersonnelIdAndIsPrimaryTrue(p.getPersonnelId())
                                .map(pjt -> pjt.getJobType().getJobTypeName()).orElse("N/A");
            return CompanyMapper.toCompanyPersonnelDto(p, primaryJob);
        }).collect(Collectors.toList());

        // Fetch recent violations
        List<SafetyIncident> recentIncidentEntities = safetyIncidentRepository.findTopNByCompanyCompanyIdOrderByIncidentDateDesc(id, PageRequest.of(0,5)); // Example: top 5
        List<SafetyIncidentDto> recentIncidentDtos = recentIncidentEntities.stream()
            .map(PersonnelMapper::toSafetyIncidentDto) // Using PersonnelMapper as it has safety incident mapping
            .collect(Collectors.toList());

        // Project history (static for now, as in CompanyDashboardService)
        List<ProjectHistoryDto> projectHistory = companyDashboardService.getProjectHistory(id);


        return CompanyMapper.toCompanyDetailDto(company, statsDto, keyPersonnelDtos, projectHistory, recentIncidentDtos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyBasicDto> getAllCompanies(Map<String, String> searchCriteria) {
        Specification<Company> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (searchCriteria != null) {
                searchCriteria.forEach((key, value) -> {
                    if (value != null && !value.isEmpty()) {
                        String lowerValue = value.toLowerCase();
                        if ("companyName".equalsIgnoreCase(key)) {
                            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("companyName")), "%" + lowerValue + "%"));
                        }
                        if ("unifiedBusinessNumber".equalsIgnoreCase(key)) {
                            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("unifiedBusinessNumber")), "%" + lowerValue + "%"));
                        }
                        if ("companyType".equalsIgnoreCase(key)) {
                            try {
                                CompanyType type = CompanyType.valueOf(value.toUpperCase());
                                predicates.add(criteriaBuilder.equal(root.get("companyType"), type));
                            } catch (IllegalArgumentException e) {
                                // Log or ignore
                            }
                        }
                    }
                });
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        List<Company> companies = companyRepository.findAll(spec);
        return companies.stream().map(company -> {
            long personnelCount = personnelRepository.countByMainCompanyCompanyIdOrSubCompanyCompanyId(company.getCompanyId(), company.getCompanyId());
            return CompanyMapper.toCompanyBasicDto(company, personnelCount);
        }).collect(Collectors.toList());
    }

    @Override
    public CompanyDetailDto createCompany(CompanyCreateDto dto) {
        Company parentCompany = null;
        if (dto.getParentCompanyId() != null) {
            parentCompany = companyRepository.findById(dto.getParentCompanyId())
                .orElseThrow(() -> new RuntimeException("Parent company not found with id: " + dto.getParentCompanyId()));
        }
        // Check for unique constraints e.g. unifiedBusinessNumber
        if (dto.getUnifiedBusinessNumber() != null && companyRepository.existsByUnifiedBusinessNumber(dto.getUnifiedBusinessNumber())) {
            throw new RuntimeException("Company with Unified Business Number " + dto.getUnifiedBusinessNumber() + " already exists.");
        }

        Company company = CompanyMapper.fromCompanyCreateDto(dto, parentCompany);
        Company savedCompany = companyRepository.save(company);
        return getCompanyById(savedCompany.getCompanyId());
    }

    @Override
    public CompanyDetailDto updateCompany(Long id, CompanyUpdateDto dto) {
        Company company = companyRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Company not found with id: " + id));
        Company parentCompany = null;
        if (dto.getParentCompanyId() != null) {
            parentCompany = companyRepository.findById(dto.getParentCompanyId())
                .orElseThrow(() -> new RuntimeException("Parent company not found with id: " + dto.getParentCompanyId()));
        }

        CompanyMapper.updateCompanyFromDto(company, dto, parentCompany);
        Company updatedCompany = companyRepository.save(company);
        return getCompanyById(updatedCompany.getCompanyId());
    }

    @Override
    public void deleteCompany(Long id) {
        Company company = companyRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Company not found with id: " + id));
        // Add checks: e.g., cannot delete company if it has active personnel or is a parent to others.
        if (personnelRepository.countByMainCompanyCompanyIdOrSubCompanyCompanyId(id, id) > 0) {
            throw new RuntimeException("Cannot delete company: It has associated personnel.");
        }
        if (companyRepository.existsByParentCompanyCompanyId(id)){
            throw new RuntimeException("Cannot delete company: It is a parent company to others.");
        }
        companyRepository.delete(company);
    }
}

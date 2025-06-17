package com.tsmc.labor_manpower_api.mapper;

import com.tsmc.labor_manpower_api.dto.*;
import com.tsmc.labor_manpower_api.model.entity.Company;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CompanyMapper {

    public static CompanyBasicDto toCompanyBasicDto(Company company, Long personnelCount) {
        if (company == null) {
            return null;
        }
        CompanyBasicDto dto = new CompanyBasicDto();
        dto.setCompanyId(company.getCompanyId());
        dto.setCompanyName(company.getCompanyName());
        dto.setCompanyType(company.getCompanyType());
        dto.setUnifiedBusinessNumber(company.getUnifiedBusinessNumber());
        dto.setEvaluationScore(company.getEvaluationScore());
        dto.setPersonnelCount(personnelCount); // Needs to be calculated and passed in
        return dto;
    }

    // Overloaded version if personnel count is not readily available or needed
    public static CompanyBasicDto toCompanyBasicDto(Company company) {
        return toCompanyBasicDto(company, null); // Pass null or a default for personnelCount
    }

    public static List<CompanyBasicDto> toCompanyBasicDtoList(List<Company> companies) {
         // Simplified: personnel count would ideally be fetched in a batch or through a specific query
        return companies.stream()
            .map(c -> toCompanyBasicDto(c, 0L /* Placeholder for personnel count */))
            .collect(Collectors.toList());
    }

    public static CompanyDetailDto toCompanyDetailDto(Company company,
                                                    CompanyPageStatsDto stats,
                                                    List<CompanyPersonnelDto> keyPersonnel,
                                                    List<ProjectHistoryDto> projectHistory,
                                                    List<SafetyIncidentDto> recentViolations) {
        if (company == null) {
            return null;
        }
        CompanyDetailDto dto = new CompanyDetailDto();
        dto.setCompanyId(company.getCompanyId());
        dto.setCompanyName(company.getCompanyName());
        dto.setCompanyType(company.getCompanyType());
        dto.setUnifiedBusinessNumber(company.getUnifiedBusinessNumber());
        dto.setEvaluationScore(company.getEvaluationScore());
        dto.setContactPerson(company.getContactPerson());
        dto.setContactPhone(company.getContactPhone());
        dto.setContactEmail(company.getContactEmail());
        dto.setAddress(company.getAddress());
        if (company.getParentCompany() != null) {
            dto.setParentCompanyId(company.getParentCompany().getCompanyId());
            dto.setParentCompanyName(company.getParentCompany().getCompanyName());
        }

        if (stats != null) {
            dto.setPersonnelCount(stats.getPersonnelCount());
            dto.setMaxAttendance(stats.getMaxAttendance());
            dto.setAvgAttendance(stats.getAvgAttendance());
            dto.setHighRiskPersonnelCount(stats.getHighRiskPersonnelCount());
            dto.setViolationPersonnelPercentage(stats.getViolationPersonnelPercentage());
        }

        dto.setRegisteredCapital(company.getRegisteredCapital());
        dto.setIndustryType(company.getIndustryType());
        dto.setWebsite(company.getWebsite());
        dto.setRemarks(company.getRemarks());

        dto.setKeyPersonnel(keyPersonnel != null ? keyPersonnel : Collections.emptyList());
        dto.setProjectHistory(projectHistory != null ? projectHistory : Collections.emptyList());
        dto.setRecentViolations(recentViolations != null ? recentViolations : Collections.emptyList());

        return dto;
    }

    public static Company fromCompanyCreateDto(CompanyCreateDto dto, Company parentCompany) {
        if (dto == null) {
            return null;
        }
        Company company = new Company();
        company.setCompanyName(dto.getCompanyName());
        company.setCompanyType(dto.getCompanyType());
        company.setAddress(dto.getAddress());
        company.setContactPerson(dto.getContactPerson());
        company.setContactPhone(dto.getContactPhone());
        company.setContactEmail(dto.getContactEmail());
        company.setUnifiedBusinessNumber(dto.getUnifiedBusinessNumber());
        company.setEvaluationScore(dto.getEvaluationScore());
        company.setRegisteredCapital(dto.getRegisteredCapital());
        company.setIndustryType(dto.getIndustryType());
        company.setWebsite(dto.getWebsite());
        company.setRemarks(dto.getRemarks());
        company.setParentCompany(parentCompany);
        return company;
    }

    public static void updateCompanyFromDto(Company company, CompanyUpdateDto dto, Company parentCompany) {
        if (dto == null || company == null) {
            return;
        }
        company.setCompanyName(dto.getCompanyName());
        // company.setCompanyType(dto.getCompanyType()); // Type might not be updatable
        company.setAddress(dto.getAddress());
        company.setContactPerson(dto.getContactPerson());
        company.setContactPhone(dto.getContactPhone());
        company.setContactEmail(dto.getContactEmail());
        company.setEvaluationScore(dto.getEvaluationScore());
        company.setRegisteredCapital(dto.getRegisteredCapital());
        company.setIndustryType(dto.getIndustryType());
        company.setWebsite(dto.getWebsite());
        company.setRemarks(dto.getRemarks());
        company.setParentCompany(parentCompany);
    }

    public static CompanyPersonnelDto toCompanyPersonnelDto(Personnel personnel, String primaryJobTypeName) {
        if (personnel == null) return null;
        CompanyPersonnelDto dto = new CompanyPersonnelDto();
        dto.setPersonnelId(personnel.getPersonnelId());
        dto.setName(personnel.getName());
        dto.setWorkId(personnel.getWorkId());
        dto.setPrimaryJobTypeName(primaryJobTypeName); // Needs to be determined
        dto.setTsmcStartDate(personnel.getTsmcStartDate());
        if (personnel.getCurrentStatus() != null) {
            dto.setCurrentStatus(personnel.getCurrentStatus().name());
        }
        return dto;
    }
}

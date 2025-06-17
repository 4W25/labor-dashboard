package com.tsmc.labor_manpower_api.service;

import com.tsmc.labor_manpower_api.dto.CompanyBasicDto;
import com.tsmc.labor_manpower_api.dto.CompanyCreateDto;
import com.tsmc.labor_manpower_api.dto.CompanyDetailDto;
import com.tsmc.labor_manpower_api.dto.CompanyUpdateDto;

import java.util.List;
import java.util.Map;

public interface CompanyService {
    CompanyDetailDto getCompanyById(Long id);
    List<CompanyBasicDto> getAllCompanies(Map<String, String> searchCriteria); // Combined
    CompanyDetailDto createCompany(CompanyCreateDto companyCreateDto);
    CompanyDetailDto updateCompany(Long id, CompanyUpdateDto companyUpdateDto);
    void deleteCompany(Long id);
    // searchCompanies is now part of getAllCompanies
    // Methods for Company Info page (4.html) will be added here - some might be part of CompanyDetailDto
}

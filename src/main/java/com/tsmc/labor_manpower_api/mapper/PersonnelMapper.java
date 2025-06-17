package com.tsmc.labor_manpower_api.mapper;

import com.tsmc.labor_manpower_api.dto.*;
import com.tsmc.labor_manpower_api.model.entity.*;
import com.tsmc.labor_manpower_api.model.enums.PersonnelStatus;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit; // Added for ChronoUnit
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PersonnelMapper {

    public static PersonnelBasicDto toPersonnelBasicDto(Personnel personnel, String primaryJobTypeName) {
        if (personnel == null) {
            return null;
        }
        PersonnelBasicDto dto = new PersonnelBasicDto();
        dto.setPersonnelId(personnel.getPersonnelId());
        dto.setName(personnel.getName());
        dto.setWorkId(personnel.getWorkId()); //工號
        dto.setIdNumber(personnel.getIdNumber()); //身分證 (Added for 2.html)
        dto.setJobTitle(personnel.getJobTitle()); //職務類別 (Added for 2.html)


        if (personnel.getMainCompany() != null) {
            dto.setMainCompanyName(personnel.getMainCompany().getCompanyName());
        }
        if (personnel.getSubCompany() != null) {
            dto.setSubCompanyName(personnel.getSubCompany().getCompanyName());
        }
        dto.setPrimaryJobTypeName(primaryJobTypeName);
        if (personnel.getCurrentStatus() != null) {
            dto.setCurrentStatus(personnel.getCurrentStatus().name());
        }

        // Calculate TSMC Experience
        if (personnel.getTsmcStartDate() != null) {
            Period period = Period.between(personnel.getTsmcStartDate(), LocalDate.now());
            int years = period.getYears();
            int months = period.getMonths();
            long totalDays = ChronoUnit.DAYS.between(personnel.getTsmcStartDate(), LocalDate.now());

            if (years > 0) {
                dto.setTsmcExperience(String.format("%d年%d月", years, months));
            } else if (months > 0) {
                 dto.setTsmcExperience(String.format("%d月", months));
            } else {
                dto.setTsmcExperience(String.format("%d天", totalDays));
            }
        } else {
            dto.setTsmcExperience("N/A");
        }

        // Placeholder for certificationsSummary - requires fetching and processing PersonnelCertifications
        dto.setCertificationsSummary("N/A"); // e.g. "高壓電證照" - needs logic

        dto.setHealthGrade(personnel.getHealthGrade()); //健康分級

        // Placeholder for safetyRecord - could be a summary string or based on incident count
        dto.setSafetyRecord(personnel.getSafetyRecordSummary() != null ? personnel.getSafetyRecordSummary() : "良好"); // e.g. "良好"

        return dto;
    }

    // toPersonnelBasicDtoList in PersonnelServiceImpl will now call this richer mapper.

    // Updated to include counts and handle more fields for PersonnelDetailDto
    public static PersonnelDetailDto toPersonnelDetailDto(Personnel personnel,
                                                        List<JobTypeDto> jobTypes,
                                                        List<CertificationDto> certifications,
                                                        List<SafetyIncidentDto> safetyIncidents,
                                                        long validCertCount,
                                                        long safetyIncidentTotalCount,
                                                        List<com.tsmc.labor_manpower_api.dto.response.PersonnelCardStatusDto> cardStatuses, // Assuming DTO path
                                                        List<com.tsmc.labor_manpower_api.dto.response.PersonnelProjectHistoryDto> projectHistory // Assuming DTO path
                                                        ) {
        if (personnel == null) {
            return null;
        }
        PersonnelDetailDto dto = new PersonnelDetailDto();
        dto.setPersonnelId(personnel.getPersonnelId());
        dto.setName(personnel.getName());
        dto.setWorkId(personnel.getWorkId());
        dto.setIdNumber(personnel.getIdNumber());

        if (personnel.getMainCompany() != null) {
            dto.setMainCompanyName(personnel.getMainCompany().getCompanyName());
        }
        if (personnel.getSubCompany() != null) {
            dto.setSubCompanyName(personnel.getSubCompany().getCompanyName());
        }

        dto.setDepartment(personnel.getDepartment());
        dto.setJobTitle(personnel.getJobTitle());
        dto.setNewcomerBadge(PersonnelStatus.NEWCOMER.equals(personnel.getCurrentStatus()) ? "新人" : null);


        dto.setCurrentStatus(personnel.getCurrentStatus());
        dto.setTsmcStartDate(personnel.getTsmcStartDate());
        if (personnel.getTsmcStartDate() != null) {
            Period seniority = Period.between(personnel.getTsmcStartDate(), LocalDate.now());
            dto.setSeniorityYears(seniority.getYears());
            dto.setSeniorityMonths(seniority.getMonths());
            long totalDaysExperience = ChronoUnit.DAYS.between(personnel.getTsmcStartDate(), LocalDate.now());
            if (seniority.getYears() > 0 || seniority.getMonths() > 0) {
                 dto.setTsmcExperienceDays(String.format("%d年%d月%d天", seniority.getYears(), seniority.getMonths(), seniority.getDays()));
            } else {
                dto.setTsmcExperienceDays(String.format("%d天", totalDaysExperience));
            }
        } else {
             dto.setTsmcExperienceDays("N/A");
        }

        dto.setProfilePhotoPath(personnel.getProfilePhotoPath());

        dto.setGender(personnel.getGender());
        dto.setDateOfBirth(personnel.getDateOfBirth());
        if (personnel.getDateOfBirth() != null) {
            dto.setAge(Period.between(personnel.getDateOfBirth(), LocalDate.now()).getYears());
        }
        dto.setNationality(personnel.getNationality());
        dto.setContactNumber(personnel.getContactNumber());
        dto.setEmail(personnel.getEmail());
        dto.setAddress(personnel.getAddress());
        dto.setBloodType(personnel.getBloodType());
        dto.setEmergencyContactName(personnel.getEmergencyContactName());
        dto.setEmergencyContactPhone(personnel.getEmergencyContactPhone());

        dto.setEmploymentType(personnel.getEmploymentType());
        dto.setSafetyRecordSummary(personnel.getSafetyRecordSummary());
        dto.setHealthGrade(personnel.getHealthGrade());
        dto.setRemarks(personnel.getRemarks());

        dto.setValidCertificationsCount((int) validCertCount);
        dto.setSafetyIncidentsCount((int) safetyIncidentTotalCount);

        dto.setKeyJobTypes(jobTypes != null ? jobTypes.stream().map(JobTypeDto::getJobTypeName).collect(Collectors.toList()) : Collections.emptyList());
        dto.setCertifications(certifications != null ? certifications : Collections.emptyList());
        dto.setSafetyIncidents(safetyIncidents != null ? safetyIncidents : Collections.emptyList());

        dto.setCardStatuses(cardStatuses != null ? cardStatuses : Collections.emptyList());
        dto.setProjectHistory(projectHistory != null ? projectHistory : Collections.emptyList());

        return dto;
    }

    public static Personnel fromPersonnelCreateDto(PersonnelCreateDto dto, Company mainCompany, Company subCompany, User user) {
        if (dto == null) {
            return null;
        }
        Personnel personnel = new Personnel();
        personnel.setName(dto.getName());
        personnel.setWorkId(dto.getWorkId());
        personnel.setIdNumber(dto.getIdNumber());
        personnel.setGender(dto.getGender());
        personnel.setDateOfBirth(dto.getDateOfBirth());
        personnel.setNationality(dto.getNationality());
        personnel.setContactNumber(dto.getContactNumber());
        personnel.setEmail(dto.getEmail());
        personnel.setEmergencyContactName(dto.getEmergencyContactName());
        personnel.setEmergencyContactPhone(dto.getEmergencyContactPhone());
        personnel.setDepartment(dto.getDepartment());
        personnel.setJobTitle(dto.getJobTitle());
        personnel.setEmploymentType(dto.getEmploymentType());
        personnel.setTsmcStartDate(dto.getTsmcStartDate());
        personnel.setCurrentStatus(dto.getCurrentStatus() != null ? dto.getCurrentStatus() : PersonnelStatus.NEWCOMER);
        personnel.setHealthGrade(dto.getHealthGrade());
        personnel.setProfilePhotoPath(dto.getProfilePhotoPath());
        personnel.setBloodType(dto.getBloodType());
        personnel.setAddress(dto.getAddress());
        personnel.setRemarks(dto.getRemarks());
        personnel.setMainCompany(mainCompany);
        personnel.setSubCompany(subCompany);
        personnel.setUser(user);
        return personnel;
    }

    public static void updatePersonnelFromDto(Personnel personnel, PersonnelUpdateDto dto, Company mainCompany, Company subCompany) {
        if (dto == null || personnel == null) {
            return;
        }
        personnel.setName(dto.getName());
        // workId and idNumber are typically not updated or have special handling
        // personnel.setWorkId(dto.getWorkId());
        // personnel.setIdNumber(dto.getIdNumber());
        personnel.setGender(dto.getGender());
        personnel.setDateOfBirth(dto.getDateOfBirth());
        personnel.setNationality(dto.getNationality());
        personnel.setContactNumber(dto.getContactNumber());
        personnel.setEmail(dto.getEmail());
        personnel.setEmergencyContactName(dto.getEmergencyContactName());
        personnel.setEmergencyContactPhone(dto.getEmergencyContactPhone());
        personnel.setDepartment(dto.getDepartment());
        personnel.setJobTitle(dto.getJobTitle());
        personnel.setEmploymentType(dto.getEmploymentType());
        // tsmcStartDate is typically not updated
        // personnel.setTsmcStartDate(dto.getTsmcStartDate());
        personnel.setCurrentStatus(dto.getCurrentStatus());
        personnel.setHealthGrade(dto.getHealthGrade());
        personnel.setProfilePhotoPath(dto.getProfilePhotoPath());
        personnel.setBloodType(dto.getBloodType());
        personnel.setAddress(dto.getAddress());
        personnel.setRemarks(dto.getRemarks());
        if (mainCompany != null) personnel.setMainCompany(mainCompany);
        personnel.setSubCompany(subCompany); // Can be set to null
    }

    public static JobTypeDto toJobTypeDto(PersonnelJobType pjt) {
        if (pjt == null || pjt.getJobType() == null) return null;
        JobTypeDto dto = new JobTypeDto();
        JobType jt = pjt.getJobType();
        dto.setJobTypeId(jt.getJobTypeId());
        dto.setJobTypeName(jt.getJobTypeName());
        dto.setJobTypeCode(jt.getJobTypeCode());
        dto.setPrimary(pjt.isPrimary());
        dto.setExperienceYears(pjt.getExperienceYears());
        return dto;
    }

    public static CertificationDto toCertificationDto(PersonnelCertification pc) {
        if (pc == null || pc.getCertification() == null) return null;
        CertificationDto dto = new CertificationDto();
        Certification cert = pc.getCertification();
        dto.setPersonnelCertificationId(pc.getPersonnelCertificationId());
        dto.setCertificationName(cert.getCertificationName());
        dto.setCertificationNumber(pc.getCertificationNumber());
        dto.setIssueDate(pc.getIssueDate());
        dto.setExpiryDate(pc.getExpiryDate());
        dto.setStatus(pc.getStatus());
        dto.setIssuingAuthority(cert.getIssuingAuthority());
        dto.setScannedCopyPath(pc.getScannedCopyPath());
        return dto;
    }

    public static SafetyIncidentDto toSafetyIncidentDto(SafetyIncident si) {
        if (si == null) return null;
        SafetyIncidentDto dto = new SafetyIncidentDto();
        dto.setIncidentId(si.getIncidentId());
        dto.setIncidentDate(si.getIncidentDate());
        dto.setLocation(si.getLocation());
        dto.setIncidentType(si.getIncidentType());
        dto.setDescription(si.getDescription());
        dto.setSeverityLevel(si.getSeverityLevel());
        dto.setStatus(si.getStatus());
        if (si.getCompany() != null) {
            dto.setCompanyName(si.getCompany().getCompanyName());
        }
        return dto;
    }
}

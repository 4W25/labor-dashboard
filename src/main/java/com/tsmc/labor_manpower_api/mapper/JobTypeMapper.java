package com.tsmc.labor_manpower_api.mapper;

import com.tsmc.labor_manpower_api.dto.JobTypeDto;
import com.tsmc.labor_manpower_api.model.entity.JobType;
import com.tsmc.labor_manpower_api.model.entity.PersonnelJobType;

import java.util.List;
import java.util.stream.Collectors;

public class JobTypeMapper {

    public static JobTypeDto toJobTypeDto(JobType jobType) {
        if (jobType == null) {
            return null;
        }
        return new JobTypeDto(
            jobType.getJobTypeId(),
            jobType.getJobTypeName(),
            jobType.getJobTypeCode()
        );
    }

    public static List<JobTypeDto> toJobTypeDtoList(List<JobType> jobTypes) {
        if (jobTypes == null) {
            return null;
        }
        return jobTypes.stream()
            .map(JobTypeMapper::toJobTypeDto)
            .collect(Collectors.toList());
    }

    // This method is already in PersonnelMapper, but can be here too if focused on JobType context
    public static JobTypeDto toJobTypeDto(PersonnelJobType pjt) {
        if (pjt == null || pjt.getJobType() == null) return null;
        JobType jt = pjt.getJobType();
        JobTypeDto dto = new JobTypeDto();
        dto.setJobTypeId(jt.getJobTypeId());
        dto.setJobTypeName(jt.getJobTypeName());
        dto.setJobTypeCode(jt.getJobTypeCode());
        dto.setPrimary(pjt.isPrimary());
        dto.setExperienceYears(pjt.getExperienceYears());
        return dto;
    }
}

package com.tsmc.labor_manpower_api.service.impl;

import com.tsmc.labor_manpower_api.dto.JobTypeDto;
import com.tsmc.labor_manpower_api.mapper.JobTypeMapper;
import com.tsmc.labor_manpower_api.model.entity.JobType;
import com.tsmc.labor_manpower_api.repository.JobTypeRepository;
import com.tsmc.labor_manpower_api.service.JobTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class JobTypeServiceImpl implements JobTypeService {

    private final JobTypeRepository jobTypeRepository;

    @Autowired
    public JobTypeServiceImpl(JobTypeRepository jobTypeRepository) {
        this.jobTypeRepository = jobTypeRepository;
    }

    @Override
    public List<JobTypeDto> getAllJobTypes() {
        List<JobType> jobTypes = jobTypeRepository.findAll();
        return JobTypeMapper.toJobTypeDtoList(jobTypes);
    }
}

package com.tsmc.labor_manpower_api.service;

import com.tsmc.labor_manpower_api.dto.JobTypeDto;
import java.util.List;

public interface JobTypeService {
    List<JobTypeDto> getAllJobTypes();
}

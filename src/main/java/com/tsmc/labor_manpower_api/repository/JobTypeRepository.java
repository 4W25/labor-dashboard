package com.tsmc.labor_manpower_api.repository;

import com.tsmc.labor_manpower_api.model.entity.JobType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface JobTypeRepository extends JpaRepository<JobType, Long> {
    Optional<JobType> findByJobTypeCode(String jobTypeCode);
    Optional<JobType> findByJobTypeName(String jobTypeName);
}

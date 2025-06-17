package com.tsmc.labor_manpower_api.repository;

import com.tsmc.labor_manpower_api.model.entity.PersonnelJobType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PersonnelJobTypeRepository extends JpaRepository<PersonnelJobType, Long> {
    List<PersonnelJobType> findByPersonnelPersonnelId(Long personnelId);
    List<PersonnelJobType> findByJobTypeJobTypeId(Long jobTypeId);
    Optional<PersonnelJobType> findByPersonnelPersonnelIdAndIsPrimaryTrue(Long personnelId);
    List<PersonnelJobType> findByPersonnelPersonnelIdIn(List<Long> personnelIds); // Added in previous DTO step, ensure it's here

    @Query("SELECT jt.jobTypeName as jobTypeName, COUNT(pjt.personnelJobTypeId) as count FROM PersonnelJobType pjt JOIN pjt.jobType jt GROUP BY jt.jobTypeName")
    List<com.tsmc.labor_manpower_api.repository.projection.JobTypeDistributionProjection> findJobTypeDistribution();

    @Modifying
    @Query("DELETE FROM PersonnelJobType pjt WHERE pjt.personnel.personnelId = :personnelId")
    void deleteByPersonnelPersonnelId(@Param("personnelId") Long personnelId);
}

package com.tsmc.labor_manpower_api.repository;

import com.tsmc.labor_manpower_api.model.entity.PersonnelCertification;
import com.tsmc.labor_manpower_api.model.enums.CertificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.time.LocalDate;

public interface PersonnelCertificationRepository extends JpaRepository<PersonnelCertification, Long> {
    List<PersonnelCertification> findByPersonnelPersonnelId(Long personnelId);
    List<PersonnelCertification> findByCertificationCertificationId(Long certificationId);
    List<PersonnelCertification> findByPersonnelPersonnelIdAndStatus(Long personnelId, CertificationStatus status);
    List<PersonnelCertification> findByExpiryDateBefore(LocalDate date);
    List<PersonnelCertification> findByExpiryDateBetween(LocalDate startDate, LocalDate endDate);
    long countByPersonnelPersonnelIdAndStatus(Long personnelId, CertificationStatus status); // Added for PersonnelDetailDto

    @Modifying
    @Query("DELETE FROM PersonnelCertification pc WHERE pc.personnel.personnelId = :personnelId")
    void deleteByPersonnelPersonnelId(@Param("personnelId") Long personnelId);
}

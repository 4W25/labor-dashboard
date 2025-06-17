package com.tsmc.labor_manpower_api.repository;

import com.tsmc.labor_manpower_api.model.entity.Certification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CertificationRepository extends JpaRepository<Certification, Long> {
    Optional<Certification> findByCertificationName(String certificationName);
}

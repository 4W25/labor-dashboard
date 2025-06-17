package com.tsmc.labor_manpower_api.service.impl;

import com.tsmc.labor_manpower_api.dto.*;
import com.tsmc.labor_manpower_api.mapper.PersonnelMapper;
import com.tsmc.labor_manpower_api.model.entity.*;
import com.tsmc.labor_manpower_api.repository.*;
import com.tsmc.labor_manpower_api.service.PersonnelService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional // Good practice for service layer methods that modify data
public class PersonnelServiceImpl implements PersonnelService {

    private final PersonnelRepository personnelRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final PersonnelJobTypeRepository personnelJobTypeRepository;
    private final PersonnelCertificationRepository personnelCertificationRepository;
    private final SafetyIncidentRepository safetyIncidentRepository;
    private final JobTypeRepository jobTypeRepository; // For search by job type name


    @Autowired
    public PersonnelServiceImpl(PersonnelRepository personnelRepository,
                                CompanyRepository companyRepository,
                                UserRepository userRepository,
                                PersonnelJobTypeRepository personnelJobTypeRepository,
                                PersonnelCertificationRepository personnelCertificationRepository,
                                SafetyIncidentRepository safetyIncidentRepository,
                                JobTypeRepository jobTypeRepository) {
        this.personnelRepository = personnelRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.personnelJobTypeRepository = personnelJobTypeRepository;
        this.personnelCertificationRepository = personnelCertificationRepository;
        this.safetyIncidentRepository = safetyIncidentRepository;
        this.jobTypeRepository = jobTypeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PersonnelDetailDto getPersonnelById(Long id) {
        Personnel personnel = personnelRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Personnel not found with id: " + id));

        List<JobTypeDto> jobTypes = personnelJobTypeRepository.findByPersonnelPersonnelId(id).stream()
            .map(PersonnelMapper::toJobTypeDto)
            .collect(Collectors.toList());
        List<CertificationDto> certifications = personnelCertificationRepository.findByPersonnelPersonnelId(id).stream()
            .map(PersonnelMapper::toCertificationDto)
            .collect(Collectors.toList());
        List<SafetyIncidentDto> incidents = safetyIncidentRepository.findByPersonnelPersonnelId(id).stream()
            .map(PersonnelMapper::toSafetyIncidentDto)
            .collect(Collectors.toList());

        long validCertCount = personnelCertificationRepository.countByPersonnelPersonnelIdAndStatus(id, com.tsmc.labor_manpower_api.model.enums.CertificationStatus.VALID);
        long safetyIncidentTotalCount = safetyIncidentRepository.countByPersonnelPersonnelId(id);

        // Dummy data for cardStatuses and projectHistory as per subtask instructions
        List<com.tsmc.labor_manpower_api.dto.response.PersonnelCardStatusDto> cardStatuses = new ArrayList<>();
        // Example: cardStatuses.add(new com.tsmc.labor_manpower_api.dto.response.PersonnelCardStatusDto(LocalDate.now(), "關卡", "測試原因"));

        List<com.tsmc.labor_manpower_api.dto.response.PersonnelProjectHistoryDto> projectHistory = new ArrayList<>();
        // Example: projectHistory.add(new com.tsmc.labor_manpower_api.dto.response.PersonnelProjectHistoryDto("某專案", "A公司", "B公司", LocalDate.now().minusYears(1), LocalDate.now()));


        return PersonnelMapper.toPersonnelDetailDto(personnel, jobTypes, certifications, incidents, validCertCount, safetyIncidentTotalCount, cardStatuses, projectHistory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonnelBasicDto> getAllPersonnel(Map<String, String> searchCriteria) {
        Specification<Personnel> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (searchCriteria != null) {
                searchCriteria.forEach((key, value) -> {
                    if (value != null && !value.isEmpty()) {
                        String lowerValue = value.toLowerCase();
                        if ("name".equalsIgnoreCase(key)) {
                            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + lowerValue + "%"));
                        }
                        if ("idNumber".equalsIgnoreCase(key)) {
                            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("idNumber")), "%" + lowerValue + "%"));
                        }
                        if ("workId".equalsIgnoreCase(key)) {
                            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("workId")), "%" + lowerValue + "%"));
                        }
                        if ("companyName".equalsIgnoreCase(key)) { // Search in main or sub company
                            Predicate mainCompanyMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("mainCompany").get("companyName")), "%" + lowerValue + "%");
                            Predicate subCompanyMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("subCompany").get("companyName")), "%" + lowerValue + "%");
                            predicates.add(criteriaBuilder.or(mainCompanyMatch, subCompanyMatch));
                        }
                        if ("jobTypeName".equalsIgnoreCase(key)) {
                            // This requires a subquery or a join and distinct, which can be complex with Specification alone
                            // For a simpler approach if performance allows, could filter post-fetch, or add a dedicated repository method with join
                            // Using a join approach here:
                            Join<Personnel, PersonnelJobType> pjtJoin = root.join("personnelJobTypes", JoinType.LEFT); // Assuming 'personnelJobTypes' is the mapped collection in Personnel
                            Join<PersonnelJobType, JobType> jtJoin = pjtJoin.join("jobType", JoinType.LEFT);
                            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(jtJoin.get("jobTypeName")), "%" + lowerValue + "%"));
                             query.distinct(true); // Ensure distinct personnel if they match multiple job types
                        }
                         if ("currentStatus".equalsIgnoreCase(key)) {
                            try {
                                com.tsmc.labor_manpower_api.model.enums.PersonnelStatus status = com.tsmc.labor_manpower_api.model.enums.PersonnelStatus.valueOf(value.toUpperCase());
                                predicates.add(criteriaBuilder.equal(root.get("currentStatus"), status));
                            } catch (IllegalArgumentException e) {
                                // Log invalid status string or ignore
                            }
                        }
                    }
                });
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        List<Personnel> personnelList = personnelRepository.findAll(spec);

        // For PersonnelBasicDto, we need the primary job type name.
        return personnelList.stream().map(personnel -> {
            String primaryJobTypeName = personnelJobTypeRepository.findByPersonnelPersonnelIdAndIsPrimaryTrue(personnel.getPersonnelId())
                .map(pjt -> pjt.getJobType().getJobTypeName())
                .orElse("N/A"); // Or null, or some default
            return PersonnelMapper.toPersonnelBasicDto(personnel, primaryJobTypeName);
        }).collect(Collectors.toList());
    }

    @Override
    public PersonnelDetailDto createPersonnel(PersonnelCreateDto dto) {
        Company mainCompany = companyRepository.findById(dto.getMainCompanyId())
            .orElseThrow(() -> new RuntimeException("Main company not found with id: " + dto.getMainCompanyId()));
        Company subCompany = null;
        if (dto.getSubCompanyId() != null) {
            subCompany = companyRepository.findById(dto.getSubCompanyId())
                .orElseThrow(() -> new RuntimeException("Sub company not found with id: " + dto.getSubCompanyId()));
        }
        User user = null;
        if (dto.getUserId() != null) {
            user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getUserId()));
        }

        Personnel personnel = PersonnelMapper.fromPersonnelCreateDto(dto, mainCompany, subCompany, user);
        // Add logic for saving associated job types and certifications if they are part of Create DTO
        Personnel savedPersonnel = personnelRepository.save(personnel);
        return getPersonnelById(savedPersonnel.getPersonnelId()); // Fetch full details
    }

    @Override
    public PersonnelDetailDto updatePersonnel(Long id, PersonnelUpdateDto dto) {
        Personnel personnel = personnelRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Personnel not found with id: " + id));

        Company mainCompany = null;
        if (dto.getMainCompanyId() != null) {
             mainCompany = companyRepository.findById(dto.getMainCompanyId())
                .orElseThrow(() -> new RuntimeException("Main company not found with id: " + dto.getMainCompanyId()));
        }
        Company subCompany = null;
        if (dto.getSubCompanyId() != null) {
            subCompany = companyRepository.findById(dto.getSubCompanyId())
                .orElseThrow(() -> new RuntimeException("Sub company not found with id: " + dto.getSubCompanyId()));
        }

        PersonnelMapper.updatePersonnelFromDto(personnel, dto, mainCompany, subCompany);
        // Add logic for updating associated job types and certifications
        Personnel updatedPersonnel = personnelRepository.save(personnel);
        return getPersonnelById(updatedPersonnel.getPersonnelId()); // Fetch full details
    }

    @Override
    public void deletePersonnel(Long id) {
        if (!personnelRepository.existsById(id)) {
             throw new RuntimeException("Personnel not found with id: " + id);
        }
        // Handle related entities if cascading delete is not set up
        // e.g., remove associations in PersonnelJobType, PersonnelCertification, SafetyIncidents
        personnelJobTypeRepository.deleteByPersonnelPersonnelId(id);
        personnelCertificationRepository.deleteByPersonnelPersonnelId(id);
        // safetyIncidentRepository.deleteByPersonnelPersonnelId(id); // If incidents should be deleted with personnel

        personnelRepository.deleteById(id);
    }
}

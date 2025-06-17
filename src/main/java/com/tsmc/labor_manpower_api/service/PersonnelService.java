package com.tsmc.labor_manpower_api.service;

import com.tsmc.labor_manpower_api.dto.PersonnelBasicDto;
import com.tsmc.labor_manpower_api.dto.PersonnelCreateDto;
import com.tsmc.labor_manpower_api.dto.PersonnelDetailDto;
import com.tsmc.labor_manpower_api.dto.PersonnelUpdateDto;
import java.util.List;
import java.util.Map; // For search criteria

public interface PersonnelService {
    PersonnelDetailDto getPersonnelById(Long id);
    List<PersonnelBasicDto> getAllPersonnel(Map<String, String> searchCriteria); // Combined getAll and search
    PersonnelDetailDto createPersonnel(PersonnelCreateDto personnelCreateDto);
    PersonnelDetailDto updatePersonnel(Long id, PersonnelUpdateDto personnelUpdateDto);
    void deletePersonnel(Long id);
    // searchPersonnel is now part of getAllPersonnel with criteria
}

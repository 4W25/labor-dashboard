package com.tsmc.labor_manpower_api.controller;

import com.tsmc.labor_manpower_api.dto.PersonnelBasicDto;
import com.tsmc.labor_manpower_api.dto.PersonnelCreateDto;
import com.tsmc.labor_manpower_api.dto.PersonnelDetailDto;
import com.tsmc.labor_manpower_api.dto.PersonnelUpdateDto;
import com.tsmc.labor_manpower_api.service.PersonnelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/personnel")
public class PersonnelController {

    private final PersonnelService personnelService;

    @Autowired
    public PersonnelController(PersonnelService personnelService) {
        this.personnelService = personnelService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonnelDetailDto> getPersonnelById(@PathVariable Long id) {
        try {
            PersonnelDetailDto personnelDetailDto = personnelService.getPersonnelById(id);
            return ResponseEntity.ok(personnelDetailDto);
        } catch (RuntimeException e) { // Replace with specific EntityNotFoundException
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<PersonnelBasicDto>> getAllPersonnel(@RequestParam(required = false) Map<String, String> searchCriteria) {
        // if searchCriteria is null or empty, service method should handle it as "get all"
        return ResponseEntity.ok(personnelService.getAllPersonnel(searchCriteria));
    }

    @PostMapping
    public ResponseEntity<PersonnelDetailDto> createPersonnel(@RequestBody PersonnelCreateDto personnelCreateDto) {
        try {
            PersonnelDetailDto createdPersonnel = personnelService.createPersonnel(personnelCreateDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPersonnel);
        } catch (RuntimeException e) { // Replace with specific validation/creation exceptions
            // Consider more specific error responses based on exception type
            return ResponseEntity.badRequest().body(null); // Or a DTO with error message
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonnelDetailDto> updatePersonnel(@PathVariable Long id, @RequestBody PersonnelUpdateDto personnelUpdateDto) {
        try {
            PersonnelDetailDto updatedPersonnel = personnelService.updatePersonnel(id, personnelUpdateDto);
            return ResponseEntity.ok(updatedPersonnel);
        } catch (RuntimeException e) { // Replace with specific EntityNotFoundException or validation exceptions
            if (e.getMessage() != null && e.getMessage().contains("not found")) {
                 return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(null); // Or a DTO with error message
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersonnel(@PathVariable Long id) {
        try {
            personnelService.deletePersonnel(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) { // Replace with specific EntityNotFoundException
            return ResponseEntity.notFound().build();
        }
    }

    // The @GetMapping("/search") is now covered by @GetMapping with @RequestParam
    // Endpoints for 3.html (Personnel Detail Page) are covered by getPersonnelById returning PersonnelDetailDto
}

package com.tsmc.labor_manpower_api.controller;

import com.tsmc.labor_manpower_api.model.entity.Company; // Use DTOs later
import com.tsmc.labor_manpower_api.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        // Replace Company with CompanyDetailDto later
        try {
            Company company = companyService.getCompanyById(id);
            return ResponseEntity.ok(company);
        } catch (RuntimeException e) { // Catch specific exception later e.g. EntityNotFoundException
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies() {
        // Replace List<Company> with List<CompanyListDto> later
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @PostMapping
    public ResponseEntity<Company> createCompany(@RequestBody Company company) {
        // Replace Company with CreateCompanyDto and return CompanyDto
        // Add try-catch for potential validation or creation errors (e.g. unique constraint violation)
        try {
            Company createdCompany = companyService.createCompany(company);
            return ResponseEntity.status(201).body(createdCompany);
        } catch (RuntimeException e) { // Example: DataIntegrityViolationException
            return ResponseEntity.status(400).body(null); // Bad request or conflict
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable Long id, @RequestBody Company companyDetails) {
        // Replace Company with UpdateCompanyDto and return CompanyDto
        try {
            Company updatedCompany = companyService.updateCompany(id, companyDetails);
            return ResponseEntity.ok(updatedCompany);
        } catch (RuntimeException e) { // Catch specific exception later
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        try {
            companyService.deleteCompany(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) { // Catch specific exception
            return ResponseEntity.notFound().build(); // Or handle conflicts if company has relations
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Company>> searchCompanies(@RequestParam Map<String, String> searchCriteria) {
        // This would be used if there's a company search page, or for dropdown filtering.
        return ResponseEntity.ok(companyService.searchCompanies(searchCriteria));
    }
}

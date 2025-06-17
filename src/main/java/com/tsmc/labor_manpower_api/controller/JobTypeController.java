package com.tsmc.labor_manpower_api.controller;

import com.tsmc.labor_manpower_api.dto.JobTypeDto;
import com.tsmc.labor_manpower_api.service.JobTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/jobtypes")
public class JobTypeController {

    private final JobTypeService jobTypeService;

    @Autowired
    public JobTypeController(JobTypeService jobTypeService) {
        this.jobTypeService = jobTypeService;
    }

    @GetMapping
    public ResponseEntity<List<JobTypeDto>> getAllJobTypes() {
        return ResponseEntity.ok(jobTypeService.getAllJobTypes());
    }
}

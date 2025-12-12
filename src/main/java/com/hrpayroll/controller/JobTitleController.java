package com.hrpayroll.controller;

import com.hrpayroll.entity.JobTitle;
import com.hrpayroll.service.JobTitleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/job-titles")
public class JobTitleController {

    private final JobTitleService jobTitleService;

    @Autowired
    public JobTitleController(JobTitleService jobTitleService) {
        this.jobTitleService = jobTitleService;
    }

    @GetMapping
    public ResponseEntity<List<JobTitle>> getAllJobTitles() {
        List<JobTitle> jobTitles = jobTitleService.getAllJobTitles();
        return ResponseEntity.ok(jobTitles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobTitle> getJobTitleById(@PathVariable Long id) {
        JobTitle jobTitle = jobTitleService.getJobTitleById(id);
        return ResponseEntity.ok(jobTitle);
    }

    @PostMapping
    public ResponseEntity<JobTitle> createJobTitle(@RequestBody JobTitle jobTitle) {
        JobTitle created = jobTitleService.createJobTitle(jobTitle);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobTitle> updateJobTitle(
            @PathVariable Long id,
            @RequestBody JobTitle jobTitleDetails) {
        JobTitle updated = jobTitleService.updateJobTitle(id, jobTitleDetails);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobTitle(@PathVariable Long id) {
        jobTitleService.deleteJobTitle(id);
        return ResponseEntity.noContent().build();
    }
}

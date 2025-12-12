package com.hrpayroll.service;

import com.hrpayroll.entity.JobTitle;
import com.hrpayroll.exception.DatabaseException;
import com.hrpayroll.exception.ResourceNotFoundException;
import com.hrpayroll.exception.ValidationException;
import com.hrpayroll.repository.JobTitleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@SuppressWarnings("null")
public class JobTitleService {

    private static final Logger logger = LoggerFactory.getLogger(JobTitleService.class);
    
    private final JobTitleRepository jobTitleRepository;

    @Autowired
    public JobTitleService(JobTitleRepository jobTitleRepository) {
        this.jobTitleRepository = jobTitleRepository;
    }

    @Transactional(readOnly = true)
    public List<JobTitle> getAllJobTitles() {
        try {
            return jobTitleRepository.findAll();
        } catch (DataAccessException e) {
            logger.error("Error retrieving job titles", e);
            throw new DatabaseException("Failed to retrieve job titles", e);
        }
    }

    @Transactional(readOnly = true)
    public JobTitle getJobTitleById(Long id) {
        try {
            if (id == null) {
                throw new ValidationException("Job Title ID cannot be null");
            }
            return jobTitleRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("JobTitle", "id", id));
        } catch (ValidationException | ResourceNotFoundException e) {
            throw e;
        } catch (DataAccessException e) {
            logger.error("Error retrieving job title with id: {}", id, e);
            throw new DatabaseException("Failed to retrieve job title", e);
        }
    }

    @Transactional
    public JobTitle createJobTitle(JobTitle jobTitle) {
        try {
            if (jobTitle == null) {
                throw new ValidationException("Job Title cannot be null");
            }
            if (jobTitle.getTitle() == null || jobTitle.getTitle().trim().isEmpty()) {
                throw new ValidationException("Job Title name cannot be null or empty");
            }
            if (jobTitle.getBaseSalary() == null || jobTitle.getBaseSalary() <= 0) {
                throw new ValidationException("Base salary must be greater than 0");
            }
            
            if (jobTitleRepository.existsByTitle(jobTitle.getTitle())) {
                throw new ValidationException("Job Title with title '" + jobTitle.getTitle() + "' already exists");
            }
            
            return jobTitleRepository.save(jobTitle);
        } catch (ValidationException e) {
            throw e;
        } catch (DataAccessException e) {
            logger.error("Error creating job title", e);
            throw new DatabaseException("Failed to create job title", e);
        }
    }

    @Transactional
    public JobTitle updateJobTitle(Long id, JobTitle jobTitleDetails) {
        try {
            if (id == null) {
                throw new ValidationException("Job Title ID cannot be null");
            }
            if (jobTitleDetails == null) {
                throw new ValidationException("Job Title details cannot be null");
            }
            
            JobTitle jobTitle = getJobTitleById(id);
            
            if (!jobTitle.getTitle().equals(jobTitleDetails.getTitle()) &&
                jobTitleRepository.existsByTitle(jobTitleDetails.getTitle())) {
                throw new ValidationException("Job Title with title '" + jobTitleDetails.getTitle() + "' already exists");
            }
            
            if (jobTitleDetails.getBaseSalary() != null && jobTitleDetails.getBaseSalary() <= 0) {
                throw new ValidationException("Base salary must be greater than 0");
            }

            jobTitle.setTitle(jobTitleDetails.getTitle());
            jobTitle.setBaseSalary(jobTitleDetails.getBaseSalary());
            
            return jobTitleRepository.save(jobTitle);
        } catch (ValidationException | ResourceNotFoundException e) {
            throw e;
        } catch (DataAccessException e) {
            logger.error("Error updating job title with id: {}", id, e);
            throw new DatabaseException("Failed to update job title", e);
        }
    }

    @Transactional
    public void deleteJobTitle(Long id) {
        try {
            if (id == null) {
                throw new ValidationException("Job Title ID cannot be null");
            }
            
            JobTitle jobTitle = getJobTitleById(id);
            jobTitleRepository.delete(jobTitle);
        } catch (ValidationException | ResourceNotFoundException e) {
            throw e;
        } catch (DataAccessException e) {
            logger.error("Error deleting job title with id: {}", id, e);
            throw new DatabaseException("Failed to delete job title", e);
        }
    }
}

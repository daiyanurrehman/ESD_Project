package com.hrpayroll.repository;

import com.hrpayroll.entity.JobTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Lecture 8: Basic JpaRepository for JobTitle
@Repository
public interface JobTitleRepository extends JpaRepository<JobTitle, Long> {
    // Find job title by its specific title name
    JobTitle findByTitle(String title);
}

package com.hrpayroll.repository;

import com.hrpayroll.entity.PerformanceReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

// Lecture 8: JpaRepository for Performance Review entity
@Repository
public interface PerformanceReviewRepository extends JpaRepository<PerformanceReview, Long> {
    // Find all reviews for a specific employee
    List<PerformanceReview> findByEmployee_Id(Long employeeId);
    
    // Find reviews by employee and date range
    List<PerformanceReview> findByEmployee_IdAndReviewDateBetween(Long employeeId, LocalDate startDate, LocalDate endDate);
}

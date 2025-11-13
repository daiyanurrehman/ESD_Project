package com.hrpayroll.controller;

import com.hrpayroll.dto.PerformanceReviewDTO;
import com.hrpayroll.entity.PerformanceReview;
import com.hrpayroll.service.PerformanceReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

// Lecture 5: REST Controller for Performance Review CRUD operations
@RestController
@RequestMapping("/api/v1/performance-reviews")
public class PerformanceReviewController {

    private final PerformanceReviewService performanceReviewService;

    @Autowired
    public PerformanceReviewController(PerformanceReviewService performanceReviewService) {
        this.performanceReviewService = performanceReviewService;
    }

    // GET all performance reviews
    @GetMapping
    public ResponseEntity<List<PerformanceReviewDTO>> getAllPerformanceReviews() {
        List<PerformanceReviewDTO> reviews = performanceReviewService.getAllPerformanceReviews();
        return ResponseEntity.ok(reviews);
    }

    // GET performance review by ID
    @GetMapping("/{id}")
    public ResponseEntity<PerformanceReviewDTO> getPerformanceReviewById(@PathVariable Long id) {
        PerformanceReviewDTO review = performanceReviewService.getPerformanceReviewById(id);
        return ResponseEntity.ok(review);
    }

    // GET all reviews for a specific employee
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<PerformanceReviewDTO>> getReviewsByEmployeeId(@PathVariable Long employeeId) {
        List<PerformanceReviewDTO> reviews = performanceReviewService.getReviewsByEmployeeId(employeeId);
        return ResponseEntity.ok(reviews);
    }

    // GET reviews by date range for an employee
    @GetMapping("/employee/{employeeId}/date-range")
    public ResponseEntity<List<PerformanceReviewDTO>> getReviewsByDateRange(
            @PathVariable Long employeeId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        List<PerformanceReviewDTO> reviews = performanceReviewService.getReviewsByDateRange(employeeId, startDate, endDate);
        return ResponseEntity.ok(reviews);
    }

    // POST create new performance review
    @PostMapping("/employee/{employeeId}")
    public ResponseEntity<PerformanceReviewDTO> createPerformanceReview(
            @PathVariable Long employeeId,
            @RequestBody PerformanceReview performanceReview) {
        PerformanceReviewDTO newReview = performanceReviewService.createPerformanceReview(employeeId, performanceReview);
        return new ResponseEntity<>(newReview, HttpStatus.CREATED);
    }

    // PUT update performance review
    @PutMapping("/{id}")
    public ResponseEntity<PerformanceReviewDTO> updatePerformanceReview(
            @PathVariable Long id,
            @RequestBody PerformanceReview performanceReviewDetails) {
        PerformanceReviewDTO updatedReview = performanceReviewService.updatePerformanceReview(id, performanceReviewDetails);
        return ResponseEntity.ok(updatedReview);
    }

    // DELETE performance review
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerformanceReview(@PathVariable Long id) {
        performanceReviewService.deletePerformanceReview(id);
        return ResponseEntity.noContent().build();
    }
}

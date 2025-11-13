package com.hrpayroll.service;

import com.hrpayroll.dto.PerformanceReviewDTO;
import com.hrpayroll.entity.PerformanceReview;
import com.hrpayroll.entity.Employee;
import com.hrpayroll.repository.PerformanceReviewRepository;
import com.hrpayroll.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

// Lecture 9: Service for Performance Review business logic
@Service
public class PerformanceReviewService {

    private final PerformanceReviewRepository performanceReviewRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public PerformanceReviewService(PerformanceReviewRepository performanceReviewRepository, 
                                    EmployeeRepository employeeRepository) {
        this.performanceReviewRepository = performanceReviewRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional(readOnly = true)
    public List<PerformanceReviewDTO> getAllPerformanceReviews() {
        return performanceReviewRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PerformanceReviewDTO getPerformanceReviewById(Long id) {
        PerformanceReview review = performanceReviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Performance Review not found with ID: " + id));
        return convertToDTO(review);
    }

    @Transactional(readOnly = true)
    public List<PerformanceReviewDTO> getReviewsByEmployeeId(Long employeeId) {
        employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));
        
        return performanceReviewRepository.findByEmployee_Id(employeeId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PerformanceReviewDTO> getReviewsByDateRange(Long employeeId, LocalDate startDate, LocalDate endDate) {
        return performanceReviewRepository.findByEmployee_IdAndReviewDateBetween(employeeId, startDate, endDate)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PerformanceReviewDTO createPerformanceReview(Long employeeId, PerformanceReview performanceReview) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));
        
        performanceReview.setEmployee(employee);
        performanceReview.setReviewDate(LocalDate.now());
        
        PerformanceReview savedReview = performanceReviewRepository.save(performanceReview);
        return convertToDTO(savedReview);
    }

    @Transactional
    public PerformanceReviewDTO updatePerformanceReview(Long id, PerformanceReview performanceReviewDetails) {
        PerformanceReview review = performanceReviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Performance Review not found with ID: " + id));
        
        review.setScore(performanceReviewDetails.getScore());
        review.setComments(performanceReviewDetails.getComments());
        
        PerformanceReview updatedReview = performanceReviewRepository.save(review);
        return convertToDTO(updatedReview);
    }

    @Transactional
    public void deletePerformanceReview(Long id) {
        performanceReviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Performance Review not found with ID: " + id));
        performanceReviewRepository.deleteById(id);
    }

    private PerformanceReviewDTO convertToDTO(PerformanceReview review) {
        PerformanceReviewDTO dto = new PerformanceReviewDTO();
        dto.setId(review.getId());
        dto.setEmployeeName(review.getEmployee().getFirstName() + " " + review.getEmployee().getLastName());
        dto.setRating(review.getScore() != null ? review.getScore().intValue() : 0);
        dto.setComments(review.getComments());
        dto.setReviewDate(review.getReviewDate());
        dto.setDepartment(review.getEmployee().getDepartment().getName());
        return dto;
    }
}

package com.hrpayroll.service;

import com.hrpayroll.dto.PerformanceReviewDTO;
import com.hrpayroll.entity.PerformanceReview;
import com.hrpayroll.entity.Employee;
import com.hrpayroll.exception.DatabaseException;
import com.hrpayroll.exception.ResourceNotFoundException;
import com.hrpayroll.exception.ValidationException;
import com.hrpayroll.repository.PerformanceReviewRepository;
import com.hrpayroll.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

// Lecture 9: Service for Performance Review business logic
@Service
@SuppressWarnings("null")
public class PerformanceReviewService {

    private static final Logger logger = LoggerFactory.getLogger(PerformanceReviewService.class);
    
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
        try {
            return performanceReviewRepository.findAll()
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            logger.error("Error retrieving all performance reviews", e);
            throw new DatabaseException("Failed to retrieve performance reviews", e);
        } catch (Exception e) {
            logger.error("Unexpected error retrieving performance reviews", e);
            throw new DatabaseException("An unexpected error occurred while retrieving performance reviews", e);
        }
    }

    @Transactional(readOnly = true)
    public PerformanceReviewDTO getPerformanceReviewById(Long id) {
        try {
            if (id == null) {
                throw new ValidationException("Performance review ID cannot be null");
            }
            PerformanceReview review = performanceReviewRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("PerformanceReview", "id", id));
            return convertToDTO(review);
        } catch (ResourceNotFoundException | ValidationException e) {
            throw e;
        } catch (DataAccessException e) {
            logger.error("Error retrieving performance review with ID: {}", id, e);
            throw new DatabaseException("Failed to retrieve performance review", e);
        } catch (Exception e) {
            logger.error("Unexpected error retrieving performance review with ID: {}", id, e);
            throw new DatabaseException("An unexpected error occurred while retrieving performance review", e);
        }
    }

    @Transactional(readOnly = true)
    public List<PerformanceReviewDTO> getReviewsByEmployeeId(Long employeeId) {
        try {
            if (employeeId == null) {
                throw new ValidationException("Employee ID cannot be null");
            }
            employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));

            return performanceReviewRepository.findByEmployee_Id(employeeId)
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (ResourceNotFoundException | ValidationException e) {
            throw e;
        } catch (DataAccessException e) {
            logger.error("Error retrieving performance reviews for employee ID: {}", employeeId, e);
            throw new DatabaseException("Failed to retrieve performance reviews", e);
        } catch (Exception e) {
            logger.error("Unexpected error retrieving performance reviews for employee ID: {}", employeeId, e);
            throw new DatabaseException("An unexpected error occurred while retrieving performance reviews", e);
        }
    }

    @Transactional(readOnly = true)
    public List<PerformanceReviewDTO> getReviewsByDateRange(Long employeeId, LocalDate startDate, LocalDate endDate) {
        try {
            if (employeeId == null) {
                throw new ValidationException("Employee ID cannot be null");
            }
            if (startDate == null) {
                throw new ValidationException("Start date cannot be null");
            }
            if (endDate == null) {
                throw new ValidationException("End date cannot be null");
            }
            if (startDate.isAfter(endDate)) {
                throw new ValidationException("Start date cannot be after end date");
            }
            return performanceReviewRepository.findByEmployee_IdAndReviewDateBetween(employeeId, startDate, endDate)
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (ValidationException e) {
            throw e;
        } catch (DataAccessException e) {
            logger.error("Error retrieving performance reviews by date range", e);
            throw new DatabaseException("Failed to retrieve performance reviews", e);
        } catch (Exception e) {
            logger.error("Unexpected error retrieving performance reviews by date range", e);
            throw new DatabaseException("An unexpected error occurred while retrieving performance reviews", e);
        }
    }

    @Transactional
    public PerformanceReviewDTO createPerformanceReview(Long employeeId, PerformanceReview performanceReview) {
        try {
            if (employeeId == null) {
                throw new ValidationException("Employee ID cannot be null");
            }
            if (performanceReview == null) {
                throw new ValidationException("Performance review cannot be null");
            }
            
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));

            performanceReview.setEmployee(employee);
            performanceReview.setReviewDate(LocalDate.now());

            PerformanceReview savedReview = performanceReviewRepository.save(performanceReview);
            return convertToDTO(savedReview);
        } catch (ResourceNotFoundException | ValidationException e) {
            throw e;
        } catch (DataAccessException e) {
            logger.error("Error creating performance review", e);
            throw new DatabaseException("Failed to create performance review", e);
        } catch (Exception e) {
            logger.error("Unexpected error creating performance review", e);
            throw new DatabaseException("An unexpected error occurred while creating performance review", e);
        }
    }

    @Transactional
    public PerformanceReviewDTO updatePerformanceReview(Long id, PerformanceReview performanceReviewDetails) {
        try {
            if (id == null) {
                throw new ValidationException("Performance review ID cannot be null");
            }
            if (performanceReviewDetails == null) {
                throw new ValidationException("Performance review details cannot be null");
            }
            
            PerformanceReview review = performanceReviewRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("PerformanceReview", "id", id));

            if (performanceReviewDetails.getScore() != null) {
                review.setScore(performanceReviewDetails.getScore());
            }
            if (performanceReviewDetails.getComments() != null) {
                review.setComments(performanceReviewDetails.getComments());
            }

            PerformanceReview updatedReview = performanceReviewRepository.save(review);
            return convertToDTO(updatedReview);
        } catch (ResourceNotFoundException | ValidationException e) {
            throw e;
        } catch (DataAccessException e) {
            logger.error("Error updating performance review with ID: {}", id, e);
            throw new DatabaseException("Failed to update performance review", e);
        } catch (Exception e) {
            logger.error("Unexpected error updating performance review with ID: {}", id, e);
            throw new DatabaseException("An unexpected error occurred while updating performance review", e);
        }
    }

    @Transactional
    public void deletePerformanceReview(Long id) {
        try {
            if (id == null) {
                throw new ValidationException("Performance review ID cannot be null");
            }
            
            if (!performanceReviewRepository.existsById(id)) {
                throw new ResourceNotFoundException("PerformanceReview", "id", id);
            }
            
            performanceReviewRepository.deleteById(id);
        } catch (ResourceNotFoundException | ValidationException e) {
            throw e;
        } catch (DataAccessException e) {
            logger.error("Error deleting performance review with ID: {}", id, e);
            throw new DatabaseException("Failed to delete performance review", e);
        } catch (Exception e) {
            logger.error("Unexpected error deleting performance review with ID: {}", id, e);
            throw new DatabaseException("An unexpected error occurred while deleting performance review", e);
        }
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

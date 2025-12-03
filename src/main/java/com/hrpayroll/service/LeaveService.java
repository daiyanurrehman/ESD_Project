package com.hrpayroll.service;

import com.hrpayroll.entity.Employee;
import com.hrpayroll.entity.LeaveRequest;
import com.hrpayroll.entity.LeaveStatus;
import com.hrpayroll.entity.LeaveType;
import com.hrpayroll.exception.BusinessLogicException;
import com.hrpayroll.exception.DatabaseException;
import com.hrpayroll.exception.ResourceNotFoundException;
import com.hrpayroll.exception.ValidationException;
import com.hrpayroll.repository.EmployeeRepository;
import com.hrpayroll.repository.LeaveRequestRepository;
import com.hrpayroll.repository.LeaveTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@SuppressWarnings("null")
public class LeaveService {

    private static final Logger logger = LoggerFactory.getLogger(LeaveService.class);
    
    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveTypeRepository leaveTypeRepository;

    @Autowired
    public LeaveService(LeaveRequestRepository leaveRequestRepository,
            EmployeeRepository employeeRepository,
            LeaveTypeRepository leaveTypeRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.employeeRepository = employeeRepository;
        this.leaveTypeRepository = leaveTypeRepository;
    }

    // Lecture 10: Atomic transaction for submitting and validation leave
    @Transactional
    public LeaveRequest applyForLeave(Long employeeId, LeaveRequest request) {
        try {
            // Validate input
            if (employeeId == null) {
                throw new ValidationException("Employee ID cannot be null");
            }
            if (request == null) {
                throw new ValidationException("Leave request cannot be null");
            }
            if (request.getLeaveType() == null || request.getLeaveType().getId() == null) {
                throw new ValidationException("Leave type is required");
            }
            if (request.getDurationDays() <= 0) {
                throw new ValidationException("Duration days must be greater than zero");
            }
            
            Employee employee;
            try {
                employee = employeeRepository.findById(employeeId)
                        .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));
            } catch (ResourceNotFoundException e) {
                throw e;
            } catch (DataAccessException e) {
                logger.error("Error retrieving employee with ID: {}", employeeId, e);
                throw new DatabaseException("Failed to retrieve employee", e);
            }

            // This line attempts to look up the LeaveType by the ID passed in the request.
            // It relies on the front-end/client providing a LeaveType object with at least
            // the ID set.
            LeaveType type;
            try {
                type = leaveTypeRepository.findById(request.getLeaveType().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("LeaveType", "id", request.getLeaveType().getId()));
            } catch (ResourceNotFoundException e) {
                throw e;
            } catch (DataAccessException e) {
                logger.error("Error retrieving leave type with ID: {}", request.getLeaveType().getId(), e);
                throw new DatabaseException("Failed to retrieve leave type", e);
            }

            // --- Business Rule Validation (Lecture 4 concept) ---
            if (request.getDurationDays() > type.getDefaultDays()) {
                throw new BusinessLogicException("Requested days (" + request.getDurationDays() + 
                        ") exceed maximum allowed (" + type.getDefaultDays() + ") for this leave type.");
            }

            request.setEmployee(employee);
            request.setLeaveType(type);
            request.setStatus(LeaveStatus.PENDING); // Initial status

            try {
                return leaveRequestRepository.save(request);
            } catch (DataAccessException e) {
                logger.error("Error saving leave request", e);
                throw new DatabaseException("Failed to save leave request", e);
            }
        } catch (ResourceNotFoundException | ValidationException | BusinessLogicException | DatabaseException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error applying for leave", e);
            throw new DatabaseException("An unexpected error occurred while applying for leave", e);
        }
    }

    // Lecture 10: Atomic transaction for status change
    @Transactional
    public LeaveRequest updateLeaveStatus(Long requestId, LeaveStatus status) {
        try {
            if (requestId == null) {
                throw new ValidationException("Leave request ID cannot be null");
            }
            if (status == null) {
                throw new ValidationException("Leave status cannot be null");
            }
            
            LeaveRequest request;
            try {
                request = leaveRequestRepository.findById(requestId)
                        .orElseThrow(() -> new ResourceNotFoundException("LeaveRequest", "id", requestId));
            } catch (ResourceNotFoundException e) {
                throw e;
            } catch (DataAccessException e) {
                logger.error("Error retrieving leave request with ID: {}", requestId, e);
                throw new DatabaseException("Failed to retrieve leave request", e);
            }

            request.setStatus(status);
            // Additional logic here: if status is APPROVED, deduct balance from employee
            // record.

            try {
                return leaveRequestRepository.save(request);
            } catch (DataAccessException e) {
                logger.error("Error updating leave request status", e);
                throw new DatabaseException("Failed to update leave request status", e);
            }
        } catch (ResourceNotFoundException | ValidationException | DatabaseException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error updating leave status", e);
            throw new DatabaseException("An unexpected error occurred while updating leave status", e);
        }
    }

    // Lecture 10: Read operation
    @Transactional(readOnly = true)
    public List<LeaveRequest> getRequestsByStatus(LeaveStatus status) {
        try {
            if (status == null) {
                throw new ValidationException("Leave status cannot be null");
            }
            return leaveRequestRepository.findByStatus(status);
        } catch (ValidationException e) {
            throw e;
        } catch (DataAccessException e) {
            logger.error("Error retrieving leave requests by status: {}", status, e);
            throw new DatabaseException("Failed to retrieve leave requests", e);
        } catch (Exception e) {
            logger.error("Unexpected error retrieving leave requests", e);
            throw new DatabaseException("An unexpected error occurred while retrieving leave requests", e);
        }
    }
}

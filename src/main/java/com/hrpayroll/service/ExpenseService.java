package com.hrpayroll.service;

import com.hrpayroll.dto.ExpenseClaimDTO;
import com.hrpayroll.entity.ExpenseClaim;
import com.hrpayroll.entity.Employee;
import com.hrpayroll.entity.LeaveStatus;
import com.hrpayroll.exception.DatabaseException;
import com.hrpayroll.exception.ResourceNotFoundException;
import com.hrpayroll.exception.ValidationException;
import com.hrpayroll.repository.ExpenseClaimRepository;
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

// Lecture 9: Service for Expense Claim business logic
@Service
@SuppressWarnings("null")
public class ExpenseService {

    private static final Logger logger = LoggerFactory.getLogger(ExpenseService.class);
    
    private final ExpenseClaimRepository expenseClaimRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public ExpenseService(ExpenseClaimRepository expenseClaimRepository,
            EmployeeRepository employeeRepository) {
        this.expenseClaimRepository = expenseClaimRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional(readOnly = true)
    public List<ExpenseClaimDTO> getAllExpenseClaims() {
        try {
            return expenseClaimRepository.findAll()
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            logger.error("Error retrieving all expense claims", e);
            throw new DatabaseException("Failed to retrieve expense claims", e);
        } catch (Exception e) {
            logger.error("Unexpected error retrieving expense claims", e);
            throw new DatabaseException("An unexpected error occurred while retrieving expense claims", e);
        }
    }

    @Transactional(readOnly = true)
    public ExpenseClaimDTO getExpenseClaimById(Long id) {
        try {
            if (id == null) {
                throw new ValidationException("Expense claim ID cannot be null");
            }
            ExpenseClaim expense = expenseClaimRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("ExpenseClaim", "id", id));
            return convertToDTO(expense);
        } catch (ResourceNotFoundException | ValidationException e) {
            throw e;
        } catch (DataAccessException e) {
            logger.error("Error retrieving expense claim with ID: {}", id, e);
            throw new DatabaseException("Failed to retrieve expense claim", e);
        } catch (Exception e) {
            logger.error("Unexpected error retrieving expense claim with ID: {}", id, e);
            throw new DatabaseException("An unexpected error occurred while retrieving expense claim", e);
        }
    }

    @Transactional(readOnly = true)
    public List<ExpenseClaimDTO> getExpenseClaimsByEmployeeId(Long employeeId) {
        try {
            if (employeeId == null) {
                throw new ValidationException("Employee ID cannot be null");
            }
            employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));

            return expenseClaimRepository.findByEmployee_Id(employeeId)
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (ResourceNotFoundException | ValidationException e) {
            throw e;
        } catch (DataAccessException e) {
            logger.error("Error retrieving expense claims for employee ID: {}", employeeId, e);
            throw new DatabaseException("Failed to retrieve expense claims", e);
        } catch (Exception e) {
            logger.error("Unexpected error retrieving expense claims for employee ID: {}", employeeId, e);
            throw new DatabaseException("An unexpected error occurred while retrieving expense claims", e);
        }
    }

    @Transactional(readOnly = true)
    public List<ExpenseClaimDTO> getExpenseClaimsByStatus(LeaveStatus status) {
        return expenseClaimRepository.findByStatus(status.name())
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ExpenseClaimDTO> getPendingExpenseClaims() {
        return expenseClaimRepository.findByStatusOrderByClaimDateDesc(LeaveStatus.PENDING.name())
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ExpenseClaimDTO> getExpenseClaimsByDateRange(LocalDate startDate, LocalDate endDate) {
        return expenseClaimRepository.findByClaimDateBetween(startDate, endDate)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ExpenseClaimDTO createExpenseClaim(Long employeeId, ExpenseClaim expenseClaim) {
        try {
            if (employeeId == null) {
                throw new ValidationException("Employee ID cannot be null");
            }
            if (expenseClaim == null) {
                throw new ValidationException("Expense claim cannot be null");
            }
            
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));

            expenseClaim.setEmployee(employee);
            expenseClaim.setClaimDate(LocalDate.now());
            expenseClaim.setStatus(LeaveStatus.PENDING);

            ExpenseClaim savedClaim = expenseClaimRepository.save(expenseClaim);
            return convertToDTO(savedClaim);
        } catch (ResourceNotFoundException | ValidationException e) {
            throw e;
        } catch (DataAccessException e) {
            logger.error("Error creating expense claim", e);
            throw new DatabaseException("Failed to create expense claim", e);
        } catch (Exception e) {
            logger.error("Unexpected error creating expense claim", e);
            throw new DatabaseException("An unexpected error occurred while creating expense claim", e);
        }
    }

    @Transactional
    public ExpenseClaimDTO updateExpenseClaim(Long id, ExpenseClaim expenseClaimDetails) {
        try {
            if (id == null) {
                throw new ValidationException("Expense claim ID cannot be null");
            }
            if (expenseClaimDetails == null) {
                throw new ValidationException("Expense claim details cannot be null");
            }
            
            ExpenseClaim claim = expenseClaimRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("ExpenseClaim", "id", id));

            if (expenseClaimDetails.getTotalAmount() != null) {
                claim.setTotalAmount(expenseClaimDetails.getTotalAmount());
            }

            ExpenseClaim updatedClaim = expenseClaimRepository.save(claim);
            return convertToDTO(updatedClaim);
        } catch (ResourceNotFoundException | ValidationException e) {
            throw e;
        } catch (DataAccessException e) {
            logger.error("Error updating expense claim with ID: {}", id, e);
            throw new DatabaseException("Failed to update expense claim", e);
        } catch (Exception e) {
            logger.error("Unexpected error updating expense claim with ID: {}", id, e);
            throw new DatabaseException("An unexpected error occurred while updating expense claim", e);
        }
    }

    @Transactional
    public ExpenseClaimDTO approveExpenseClaim(Long id) {
        try {
            if (id == null) {
                throw new ValidationException("Expense claim ID cannot be null");
            }
            
            ExpenseClaim claim = expenseClaimRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("ExpenseClaim", "id", id));

            claim.setStatus(LeaveStatus.APPROVED);

            ExpenseClaim updatedClaim = expenseClaimRepository.save(claim);
            return convertToDTO(updatedClaim);
        } catch (ResourceNotFoundException | ValidationException e) {
            throw e;
        } catch (DataAccessException e) {
            logger.error("Error approving expense claim with ID: {}", id, e);
            throw new DatabaseException("Failed to approve expense claim", e);
        } catch (Exception e) {
            logger.error("Unexpected error approving expense claim with ID: {}", id, e);
            throw new DatabaseException("An unexpected error occurred while approving expense claim", e);
        }
    }

    @Transactional
    public ExpenseClaimDTO rejectExpenseClaim(Long id) {
        try {
            if (id == null) {
                throw new ValidationException("Expense claim ID cannot be null");
            }
            
            ExpenseClaim claim = expenseClaimRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("ExpenseClaim", "id", id));

            claim.setStatus(LeaveStatus.REJECTED);

            ExpenseClaim updatedClaim = expenseClaimRepository.save(claim);
            return convertToDTO(updatedClaim);
        } catch (ResourceNotFoundException | ValidationException e) {
            throw e;
        } catch (DataAccessException e) {
            logger.error("Error rejecting expense claim with ID: {}", id, e);
            throw new DatabaseException("Failed to reject expense claim", e);
        } catch (Exception e) {
            logger.error("Unexpected error rejecting expense claim with ID: {}", id, e);
            throw new DatabaseException("An unexpected error occurred while rejecting expense claim", e);
        }
    }

    @Transactional
    public void deleteExpenseClaim(Long id) {
        try {
            if (id == null) {
                throw new ValidationException("Expense claim ID cannot be null");
            }
            
            if (!expenseClaimRepository.existsById(id)) {
                throw new ResourceNotFoundException("ExpenseClaim", "id", id);
            }
            
            expenseClaimRepository.deleteById(id);
        } catch (ResourceNotFoundException | ValidationException e) {
            throw e;
        } catch (DataAccessException e) {
            logger.error("Error deleting expense claim with ID: {}", id, e);
            throw new DatabaseException("Failed to delete expense claim", e);
        } catch (Exception e) {
            logger.error("Unexpected error deleting expense claim with ID: {}", id, e);
            throw new DatabaseException("An unexpected error occurred while deleting expense claim", e);
        }
    }

    private ExpenseClaimDTO convertToDTO(ExpenseClaim claim) {
        ExpenseClaimDTO dto = new ExpenseClaimDTO();
        dto.setId(claim.getId());
        dto.setEmployeeName(claim.getEmployee().getFirstName() + " " + claim.getEmployee().getLastName());
        dto.setTotalAmount(java.math.BigDecimal.valueOf(claim.getTotalAmount() != null ? claim.getTotalAmount() : 0));
        dto.setStatus(claim.getStatus() != null ? claim.getStatus().name() : "UNKNOWN");
        dto.setSubmissionDate(claim.getClaimDate());
        return dto;
    }
}

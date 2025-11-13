package com.hrpayroll.service;

import com.hrpayroll.dto.ExpenseClaimDTO;
import com.hrpayroll.entity.ExpenseClaim;
import com.hrpayroll.entity.Employee;
import com.hrpayroll.entity.LeaveStatus;
import com.hrpayroll.repository.ExpenseClaimRepository;
import com.hrpayroll.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

// Lecture 9: Service for Expense Claim business logic
@Service
public class ExpenseService {

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
        return expenseClaimRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ExpenseClaimDTO getExpenseClaimById(Long id) {
        ExpenseClaim expense = expenseClaimRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense Claim not found with ID: " + id));
        return convertToDTO(expense);
    }

    @Transactional(readOnly = true)
    public List<ExpenseClaimDTO> getExpenseClaimsByEmployeeId(Long employeeId) {
        employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));
        
        return expenseClaimRepository.findByEmployee_Id(employeeId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
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
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));
        
        expenseClaim.setEmployee(employee);
        expenseClaim.setClaimDate(LocalDate.now());
        expenseClaim.setStatus(LeaveStatus.PENDING);
        
        ExpenseClaim savedClaim = expenseClaimRepository.save(expenseClaim);
        return convertToDTO(savedClaim);
    }

    @Transactional
    public ExpenseClaimDTO updateExpenseClaim(Long id, ExpenseClaim expenseClaimDetails) {
        ExpenseClaim claim = expenseClaimRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense Claim not found with ID: " + id));
        
        claim.setTotalAmount(expenseClaimDetails.getTotalAmount());
        
        ExpenseClaim updatedClaim = expenseClaimRepository.save(claim);
        return convertToDTO(updatedClaim);
    }

    @Transactional
    public ExpenseClaimDTO approveExpenseClaim(Long id) {
        ExpenseClaim claim = expenseClaimRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense Claim not found with ID: " + id));
        
        claim.setStatus(LeaveStatus.APPROVED);
        
        ExpenseClaim updatedClaim = expenseClaimRepository.save(claim);
        return convertToDTO(updatedClaim);
    }

    @Transactional
    public ExpenseClaimDTO rejectExpenseClaim(Long id) {
        ExpenseClaim claim = expenseClaimRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense Claim not found with ID: " + id));
        
        claim.setStatus(LeaveStatus.REJECTED);
        
        ExpenseClaim updatedClaim = expenseClaimRepository.save(claim);
        return convertToDTO(updatedClaim);
    }

    @Transactional
    public void deleteExpenseClaim(Long id) {
        expenseClaimRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense Claim not found with ID: " + id));
        expenseClaimRepository.deleteById(id);
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

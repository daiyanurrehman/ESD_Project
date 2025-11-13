package com.hrpayroll.repository;

import com.hrpayroll.entity.ExpenseClaim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

// Lecture 8: JpaRepository for Expense Claim entity
@Repository
public interface ExpenseClaimRepository extends JpaRepository<ExpenseClaim, Long> {
    // Find all expense claims for a specific employee
    List<ExpenseClaim> findByEmployee_Id(Long employeeId);
    
    // Find expense claims by status
    List<ExpenseClaim> findByStatus(String status);
    
    // Find expense claims by date range
    List<ExpenseClaim> findByClaimDateBetween(LocalDate startDate, LocalDate endDate);
    
    // Find all pending expense claims
    List<ExpenseClaim> findByStatusOrderByClaimDateDesc(String status);
}

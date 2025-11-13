package com.hrpayroll.controller;

import com.hrpayroll.dto.ExpenseClaimDTO;
import com.hrpayroll.entity.ExpenseClaim;
import com.hrpayroll.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

// Lecture 5: REST Controller for Expense Claim CRUD operations
@RestController
@RequestMapping("/api/v1/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @Autowired
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    // GET all expense claims
    @GetMapping
    public ResponseEntity<List<ExpenseClaimDTO>> getAllExpenseClaims() {
        List<ExpenseClaimDTO> claims = expenseService.getAllExpenseClaims();
        return ResponseEntity.ok(claims);
    }

    // GET expense claim by ID
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseClaimDTO> getExpenseClaimById(@PathVariable Long id) {
        ExpenseClaimDTO claim = expenseService.getExpenseClaimById(id);
        return ResponseEntity.ok(claim);
    }

    // GET all expense claims for a specific employee
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<ExpenseClaimDTO>> getExpenseClaimsByEmployeeId(@PathVariable Long employeeId) {
        List<ExpenseClaimDTO> claims = expenseService.getExpenseClaimsByEmployeeId(employeeId);
        return ResponseEntity.ok(claims);
    }

    // GET all pending expense claims
    @GetMapping("/status/pending")
    public ResponseEntity<List<ExpenseClaimDTO>> getPendingExpenseClaims() {
        List<ExpenseClaimDTO> claims = expenseService.getPendingExpenseClaims();
        return ResponseEntity.ok(claims);
    }

    // GET expense claims by date range
    @GetMapping("/date-range")
    public ResponseEntity<List<ExpenseClaimDTO>> getExpenseClaimsByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        List<ExpenseClaimDTO> claims = expenseService.getExpenseClaimsByDateRange(startDate, endDate);
        return ResponseEntity.ok(claims);
    }

    // POST create new expense claim
    @PostMapping("/employee/{employeeId}")
    public ResponseEntity<ExpenseClaimDTO> createExpenseClaim(
            @PathVariable Long employeeId,
            @RequestBody ExpenseClaim expenseClaim) {
        ExpenseClaimDTO newClaim = expenseService.createExpenseClaim(employeeId, expenseClaim);
        return new ResponseEntity<>(newClaim, HttpStatus.CREATED);
    }

    // PUT update expense claim
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseClaimDTO> updateExpenseClaim(
            @PathVariable Long id,
            @RequestBody ExpenseClaim expenseClaimDetails) {
        ExpenseClaimDTO updatedClaim = expenseService.updateExpenseClaim(id, expenseClaimDetails);
        return ResponseEntity.ok(updatedClaim);
    }

    // PUT approve expense claim
    @PutMapping("/{id}/approve")
    public ResponseEntity<ExpenseClaimDTO> approveExpenseClaim(@PathVariable Long id) {
        ExpenseClaimDTO updatedClaim = expenseService.approveExpenseClaim(id);
        return ResponseEntity.ok(updatedClaim);
    }

    // PUT reject expense claim
    @PutMapping("/{id}/reject")
    public ResponseEntity<ExpenseClaimDTO> rejectExpenseClaim(@PathVariable Long id) {
        ExpenseClaimDTO updatedClaim = expenseService.rejectExpenseClaim(id);
        return ResponseEntity.ok(updatedClaim);
    }

    // DELETE expense claim
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpenseClaim(@PathVariable Long id) {
        expenseService.deleteExpenseClaim(id);
        return ResponseEntity.noContent().build();
    }
}

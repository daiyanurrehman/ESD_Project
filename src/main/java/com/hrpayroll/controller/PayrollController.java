package com.hrpayroll.controller;

import com.hrpayroll.entity.PaySlip;
import com.hrpayroll.service.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Lecture 5, 8: @RestController handles RESTful API requests and returns JSON
@RestController
@RequestMapping("/api/v1/payroll") // Lecture 5: Base URI mapping
public class PayrollController {

    private final PayrollService payrollService;

    // Lecture 4: DI
    @Autowired
    public PayrollController(PayrollService payrollService) {
        this.payrollService = payrollService;
    }

    /**
     * Lecture 5: @PostMapping to initiate a new resource/process (payroll calculation)
     * Access via Postman: POST http://localhost:8080/api/v1/payroll/process?month=11&year=2024
     */
    @PostMapping("/process")
    public ResponseEntity<List<PaySlip>> processPayroll(
            @RequestParam int month, // Lecture 8: @RequestParam for query parameters
            @RequestParam int year) {

        List<PaySlip> paySlips = payrollService.processMonthlyPayroll(month, year);
        // Lecture 5: ResponseEntity to control status code (201 CREATED)
        return new ResponseEntity<>(paySlips, HttpStatus.CREATED); 
    }

    /**
     * Lecture 5: @GetMapping to retrieve data (Read)
     * Lecture 8: @PathVariable to extract ID from URI
     * Access via Postman: GET http://localhost:8080/api/v1/payroll/payslips/1
     */
    @GetMapping("/payslips/{id}")
    public ResponseEntity<PaySlip> getPaySlip(@PathVariable Long id) {
        PaySlip paySlip = payrollService.getPaySlipById(id);
        return ResponseEntity.ok(paySlip); // Lecture 5: Returns 200 OK
    }
}

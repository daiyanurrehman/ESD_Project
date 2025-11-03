package com.hrpayroll.service;

import com.hrpayroll.entity.*;
import com.hrpayroll.repository.EmployeeRepository;
import com.hrpayroll.repository.PaySlipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Lecture 4, 8: Service Layer for business logic
@Service 
public class PayrollService {

    private final EmployeeRepository employeeRepository;
    private final PaySlipRepository paySlipRepository;
    
    // Lecture 4: Constructor Injection (Best Practice for DI)
    @Autowired
    public PayrollService(EmployeeRepository employeeRepository, PaySlipRepository paySlipRepository) {
        this.employeeRepository = employeeRepository;
        this.paySlipRepository = paySlipRepository;
    }

    /**
     * Lecture 10: @Transactional ensures Atomicity (all or nothing). 
     * If saving any payslip fails, the entire batch operation rolls back.
     */
    @Transactional(rollbackFor = Exception.class) 
    public List<PaySlip> processMonthlyPayroll(int month, int year) {
        // 1. Fetch all eligible employees
        List<Employee> employees = employeeRepository.findAll();
        List<PaySlip> processedPaySlips = new ArrayList<>();

        for (Employee employee : employees) {
            PaySlip paySlip = calculatePaySlip(employee, month, year);
            processedPaySlips.add(paySlip);
        }

        // 2. Save all generated payslips to the database
        // This is where the transaction is critical
        return paySlipRepository.saveAll(processedPaySlips);
    }
    
    // Lecture 10: Read operation should be readOnly for performance
    @Transactional(readOnly = true)
    public PaySlip getPaySlipById(Long id) {
        return paySlipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PaySlip not found for ID: " + id));
    }
    
    // --- Private Business Logic ---
    private PaySlip calculatePaySlip(Employee employee, int month, int year) {
        // Complex business logic: fetch base salary, deductions, attendance, etc.
        double baseSalary = employee.getJobTitle().getBaseSalary();
        double grossSalary = baseSalary; // Simplification
        double totalDeductions = baseSalary * 0.10; // Simple 10% tax deduction
        double netSalary = grossSalary - totalDeductions;
        
        PaySlip paySlip = new PaySlip();
        paySlip.setEmployee(employee);
        paySlip.setPayDate(LocalDate.of(year, month, 25));
        paySlip.setGrossSalary(grossSalary);
        paySlip.setTotalDeductions(totalDeductions);
        paySlip.setNetSalary(netSalary);
        
        // Add PaySlipItem (for detail)
        List<PaySlipItem> items = new ArrayList<>();
        PaySlipItem baseItem = createPaySlipItem("Base Salary", grossSalary, ItemType.EARNING, paySlip);
        PaySlipItem taxItem = createPaySlipItem("Income Tax Deduction", totalDeductions, ItemType.DEDUCTION, paySlip);
        items.add(baseItem);
        items.add(taxItem);
        
        paySlip.setLineItems(items);
        return paySlip;
    }

    private PaySlipItem createPaySlipItem(String desc, double amt, ItemType type, PaySlip paySlip) {
        PaySlipItem item = new PaySlipItem();
        item.setDescription(desc);
        item.setAmount(amt);
        item.setType(type);
        item.setPaySlip(paySlip);
        return item;
    }
}

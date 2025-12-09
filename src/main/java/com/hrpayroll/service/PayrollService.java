package com.hrpayroll.service;

import com.hrpayroll.dto.PaySlipDTO;
import com.hrpayroll.dto.PaySlipItemDTO;
import com.hrpayroll.entity.*;
import com.hrpayroll.exception.DatabaseException;
import com.hrpayroll.exception.ResourceNotFoundException;
import com.hrpayroll.exception.ValidationException;
import com.hrpayroll.repository.EmployeeRepository;
import com.hrpayroll.repository.PaySlipRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Lecture 4, 8: Service Layer for business logic
@Service
@SuppressWarnings("null")
public class PayrollService {

    private static final Logger logger = LoggerFactory.getLogger(PayrollService.class);
    
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
    public List<PaySlipDTO> processMonthlyPayroll(int month, int year) {
        try {
            // Validate input
            if (month < 1 || month > 12) {
                throw new ValidationException("Month must be between 1 and 12");
            }
            if (year < 2000 || year > 2100) {
                throw new ValidationException("Year must be between 2000 and 2100");
            }
            
            // 1. Fetch all eligible employees
            List<Employee> employees;
            try {
                employees = employeeRepository.findAll();
            } catch (DataAccessException e) {
                logger.error("Error retrieving employees for payroll processing", e);
                throw new DatabaseException("Failed to retrieve employees for payroll processing", e);
            }
            
            if (employees.isEmpty()) {
                logger.warn("No employees found for payroll processing");
                return new ArrayList<>();
            }
            
            List<PaySlip> processedPaySlips = new ArrayList<>();

            for (Employee employee : employees) {
                try {
                    PaySlip paySlip = calculatePaySlip(employee, month, year);
                    processedPaySlips.add(paySlip);
                } catch (Exception e) {
                    logger.error("Error calculating payslip for employee ID: {}", employee.getId(), e);
                    throw new DatabaseException("Failed to calculate payslip for employee: " + employee.getId(), e);
                }
            }

            // 2. Save all generated payslips to the database
            // This is where the transaction is critical
            try {
                return paySlipRepository.saveAll(processedPaySlips)
                        .stream()
                        .map(this::toDTO)
                        .collect(Collectors.toList());
            } catch (DataAccessException e) {
                logger.error("Error saving payslips to database", e);
                throw new DatabaseException("Failed to save payslips", e);
            }
        } catch (ValidationException | DatabaseException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error processing monthly payroll", e);
            throw new DatabaseException("An unexpected error occurred while processing payroll", e);
        }
    }

    // Lecture 10: Read operation should be readOnly for performance
    @Transactional(readOnly = true)
    public PaySlipDTO getPaySlipById(Long id) {
        try {
            if (id == null) {
                throw new ValidationException("PaySlip ID cannot be null");
            }
            PaySlip paySlip = paySlipRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("PaySlip", "id", id));
            return toDTO(paySlip);
        } catch (ResourceNotFoundException | ValidationException e) {
            throw e;
        } catch (DataAccessException e) {
            logger.error("Error retrieving payslip with ID: {}", id, e);
            throw new DatabaseException("Failed to retrieve payslip", e);
        } catch (Exception e) {
            logger.error("Unexpected error retrieving payslip with ID: {}", id, e);
            throw new DatabaseException("An unexpected error occurred while retrieving payslip", e);
        }
    }

    // --- Private Business Logic ---
    private PaySlip calculatePaySlip(Employee employee, int month, int year) {
        try {
            if (employee == null) {
                throw new ValidationException("Employee cannot be null");
            }
            if (employee.getJobTitle() == null) {
                throw new ValidationException("Employee job title is required for payroll calculation");
            }
            if (employee.getJobTitle().getBaseSalary() == null || employee.getJobTitle().getBaseSalary() <= 0) {
                throw new ValidationException("Employee base salary must be greater than zero");
            }
            
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
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error calculating payslip for employee ID: {}", employee != null ? employee.getId() : "null", e);
            throw new DatabaseException("Failed to calculate payslip", e);
        }
    }

    private PaySlipItem createPaySlipItem(String desc, double amt, ItemType type, PaySlip paySlip) {
        PaySlipItem item = new PaySlipItem();
        item.setDescription(desc);
        item.setAmount(amt);
        item.setType(type);
        item.setPaySlip(paySlip);
        return item;
    }

    private PaySlipDTO toDTO(PaySlip paySlip) {
        if (paySlip == null) {
            return null;
        }
        PaySlipDTO dto = new PaySlipDTO();
        dto.setId(paySlip.getId());
        dto.setEmployeeId(paySlip.getEmployee() != null ? paySlip.getEmployee().getId() : null);
        dto.setEmployeeName(paySlip.getEmployee() != null
                ? paySlip.getEmployee().getFirstName() + " " + paySlip.getEmployee().getLastName()
                : null);
        dto.setPayDate(paySlip.getPayDate());
        dto.setGrossSalary(paySlip.getGrossSalary());
        dto.setTotalDeductions(paySlip.getTotalDeductions());
        dto.setNetSalary(paySlip.getNetSalary());
        if (paySlip.getLineItems() != null) {
            dto.setItems(paySlip.getLineItems().stream()
                    .map(li -> new PaySlipItemDTO(
                            li.getDescription(),
                            li.getAmount(),
                            li.getType() != null ? li.getType().name() : null))
                    .collect(Collectors.toList()));
        }
        return dto;
    }
}

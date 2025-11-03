package com.hrpayroll.service;

import com.hrpayroll.entity.Employee;
import com.hrpayroll.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Lecture 4: @Service for employee management business logic
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    // Lecture 4: Dependency Injection
    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Lecture 10: Read operation for performance optimization
    @Transactional(readOnly = true)
    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }

    // Lecture 10: Read operation
    @Transactional(readOnly = true)
    public Employee findEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));
    }

    // Lecture 10: Write operation
    @Transactional
    public Employee saveEmployee(Employee employee) {
        // Here you would add validation logic (Lecture 8 concept)
        return employeeRepository.save(employee);
    }
    
    // Lecture 10: Write operation - Ensures update is atomic
    @Transactional
    public Employee updateEmployee(Long id, Employee employeeDetails) {
        Employee employee = findEmployeeById(id);
        
        // Update fields (example of partial update/PUT logic)
        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        employee.setDepartment(employeeDetails.getDepartment());
        employee.setJobTitle(employeeDetails.getJobTitle());
        
        return employeeRepository.save(employee);
    }

    // Lecture 10: Delete operation
    @Transactional
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}

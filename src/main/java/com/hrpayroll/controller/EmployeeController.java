package com.hrpayroll.controller;

import com.hrpayroll.entity.Employee;
import com.hrpayroll.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Lecture 5: REST Controller for Employee CRUD operations
@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Lecture 5: GET /api/v1/employees (Read All)
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        // Lecture 8: Service Layer delegates to Repository
        List<Employee> employees = employeeService.findAllEmployees();
        return ResponseEntity.ok(employees);
    }
    
    // Lecture 5: GET /api/v1/employees/{id} (Read One)
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.findEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    // Lecture 5: POST /api/v1/employees (Create)
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee newEmployee = employeeService.saveEmployee(employee);
        // Lecture 5: Returns 201 CREATED status
        return new ResponseEntity<>(newEmployee, HttpStatus.CREATED);
    }

    // Lecture 5: PUT /api/v1/employees/{id} (Update/Replace)
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employeeDetails) {
        // Validation and update logic is handled in the Service layer
        Employee updatedEmployee = employeeService.updateEmployee(id, employeeDetails);
        return ResponseEntity.ok(updatedEmployee);
    }

    // Lecture 5: DELETE /api/v1/employees/{id} (Delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        // Lecture 5: Returns 204 NO CONTENT status for successful deletion
        return ResponseEntity.noContent().build();
    }
}

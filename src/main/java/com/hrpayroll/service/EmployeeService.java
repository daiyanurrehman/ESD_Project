package com.hrpayroll.service;

import com.hrpayroll.dto.EmployeeDTO;
import com.hrpayroll.entity.Employee;
import com.hrpayroll.exception.DatabaseException;
import com.hrpayroll.exception.ResourceNotFoundException;
import com.hrpayroll.exception.ValidationException;
import com.hrpayroll.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

// Lecture 4: @Service for employee management business logic
@Service
@SuppressWarnings("null")
public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
    
    private final EmployeeRepository employeeRepository;

    // Lecture 4: Dependency Injection
    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Lecture 10: Read operation for performance optimization
    @Transactional(readOnly = true)
    public List<EmployeeDTO> findAllEmployees() {
        try {
            return employeeRepository.findAll()
                    .stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            logger.error("Error retrieving all employees", e);
            throw new DatabaseException("Failed to retrieve employees", e);
        } catch (Exception e) {
            logger.error("Unexpected error retrieving employees", e);
            throw new DatabaseException("An unexpected error occurred while retrieving employees", e);
        }
    }

    // Lecture 10: Read operation
    @Transactional(readOnly = true)
    public EmployeeDTO findEmployeeById(Long id) {
        try {
            if (id == null) {
                throw new ValidationException("Employee ID cannot be null");
            }
            Employee employee = employeeRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
            return toDTO(employee);
        } catch (ResourceNotFoundException | ValidationException e) {
            throw e;
        } catch (DataAccessException e) {
            logger.error("Error retrieving employee with ID: {}", id, e);
            throw new DatabaseException("Failed to retrieve employee", e);
        } catch (Exception e) {
            logger.error("Unexpected error retrieving employee with ID: {}", id, e);
            throw new DatabaseException("An unexpected error occurred while retrieving employee", e);
        }
    }

    // Lecture 10: Write operation
    @Transactional
    public EmployeeDTO saveEmployee(Employee employee) {
        try {
            // Validation logic
            if (employee == null) {
                throw new ValidationException("Employee cannot be null");
            }
            if (employee.getFirstName() == null || employee.getFirstName().trim().isEmpty()) {
                throw new ValidationException("Employee first name is required");
            }
            if (employee.getLastName() == null || employee.getLastName().trim().isEmpty()) {
                throw new ValidationException("Employee last name is required");
            }
            Employee saved = employeeRepository.save(employee);
            return toDTO(saved);
        } catch (ValidationException e) {
            throw e;
        } catch (DataAccessException e) {
            logger.error("Error saving employee", e);
            throw new DatabaseException("Failed to save employee", e);
        } catch (Exception e) {
            logger.error("Unexpected error saving employee", e);
            throw new DatabaseException("An unexpected error occurred while saving employee", e);
        }
    }

    // Lecture 10: Write operation - Ensures update is atomic
    @Transactional
    public EmployeeDTO updateEmployee(Long id, Employee employeeDetails) {
        try {
            if (id == null) {
                throw new ValidationException("Employee ID cannot be null");
            }
            if (employeeDetails == null) {
                throw new ValidationException("Employee details cannot be null");
            }
            
            // Fetch existing entity
            Employee employee = employeeRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));

            // Update fields (example of partial update/PUT logic)
            if (employeeDetails.getFirstName() != null) {
                employee.setFirstName(employeeDetails.getFirstName());
            }
            if (employeeDetails.getLastName() != null) {
                employee.setLastName(employeeDetails.getLastName());
            }
            if (employeeDetails.getDepartment() != null) {
                employee.setDepartment(employeeDetails.getDepartment());
            }
            if (employeeDetails.getJobTitle() != null) {
                employee.setJobTitle(employeeDetails.getJobTitle());
            }
            Employee updated = employeeRepository.save(employee);
            return toDTO(updated);
        } catch (ResourceNotFoundException | ValidationException e) {
            throw e;
        } catch (DataAccessException e) {
            logger.error("Error updating employee with ID: {}", id, e);
            throw new DatabaseException("Failed to update employee", e);
        } catch (Exception e) {
            logger.error("Unexpected error updating employee with ID: {}", id, e);
            throw new DatabaseException("An unexpected error occurred while updating employee", e);
        }
    }

    // Lecture 10: Delete operation
    @Transactional
    public void deleteEmployee(Long id) {
        try {
            if (id == null) {
                throw new ValidationException("Employee ID cannot be null");
            }
            
            // Check if employee exists before deleting
            if (!employeeRepository.existsById(id)) {
                throw new ResourceNotFoundException("Employee", "id", id);
            }
            
            employeeRepository.deleteById(id);
        } catch (ResourceNotFoundException | ValidationException e) {
            throw e;
        } catch (DataAccessException e) {
            logger.error("Error deleting employee with ID: {}", id, e);
            throw new DatabaseException("Failed to delete employee", e);
        } catch (Exception e) {
            logger.error("Unexpected error deleting employee with ID: {}", id, e);
            throw new DatabaseException("An unexpected error occurred while deleting employee", e);
        }
    }

    // Mapping helper
    private EmployeeDTO toDTO(Employee employee) {
        if (employee == null) {
            return null;
        }
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setUsername(employee.getUsername());
        dto.setDepartmentName(employee.getDepartment() != null ? employee.getDepartment().getName() : null);
        dto.setJobTitle(employee.getJobTitle() != null ? employee.getJobTitle().getTitle() : null);
        dto.setHireDate(employee.getHireDate());
        dto.setDateOfBirth(employee.getDateOfBirth());
        dto.setRole(employee.getRole() != null ? employee.getRole().name() : null);
        return dto;
    }
}

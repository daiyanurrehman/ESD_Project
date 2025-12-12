package com.hrpayroll.service;

import com.hrpayroll.entity.Department;
import com.hrpayroll.exception.DatabaseException;
import com.hrpayroll.exception.ResourceNotFoundException;
import com.hrpayroll.exception.ValidationException;
import com.hrpayroll.repository.DepartmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@SuppressWarnings("null")
public class DepartmentService {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentService.class);
    
    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Transactional(readOnly = true)
    public List<Department> getAllDepartments() {
        try {
            return departmentRepository.findAll();
        } catch (DataAccessException e) {
            logger.error("Error retrieving departments", e);
            throw new DatabaseException("Failed to retrieve departments", e);
        }
    }

    @Transactional(readOnly = true)
    public Department getDepartmentById(Long id) {
        try {
            if (id == null) {
                throw new ValidationException("Department ID cannot be null");
            }
            return departmentRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
        } catch (ValidationException | ResourceNotFoundException e) {
            throw e;
        } catch (DataAccessException e) {
            logger.error("Error retrieving department with id: {}", id, e);
            throw new DatabaseException("Failed to retrieve department", e);
        }
    }

    @Transactional
    public Department createDepartment(Department department) {
        try {
            if (department == null) {
                throw new ValidationException("Department cannot be null");
            }
            if (department.getName() == null || department.getName().trim().isEmpty()) {
                throw new ValidationException("Department name cannot be null or empty");
            }
            
            if (departmentRepository.existsByName(department.getName())) {
                throw new ValidationException("Department with name '" + department.getName() + "' already exists");
            }
            
            return departmentRepository.save(department);
        } catch (ValidationException e) {
            throw e;
        } catch (DataAccessException e) {
            logger.error("Error creating department", e);
            throw new DatabaseException("Failed to create department", e);
        }
    }

    @Transactional
    public Department updateDepartment(Long id, Department departmentDetails) {
        try {
            if (id == null) {
                throw new ValidationException("Department ID cannot be null");
            }
            if (departmentDetails == null) {
                throw new ValidationException("Department details cannot be null");
            }
            
            Department department = getDepartmentById(id);
            
            if (!department.getName().equals(departmentDetails.getName()) &&
                departmentRepository.existsByName(departmentDetails.getName())) {
                throw new ValidationException("Department with name '" + departmentDetails.getName() + "' already exists");
            }

            department.setName(departmentDetails.getName());
            department.setDescription(departmentDetails.getDescription());
            
            return departmentRepository.save(department);
        } catch (ValidationException | ResourceNotFoundException e) {
            throw e;
        } catch (DataAccessException e) {
            logger.error("Error updating department with id: {}", id, e);
            throw new DatabaseException("Failed to update department", e);
        }
    }

    @Transactional
    public void deleteDepartment(Long id) {
        try {
            if (id == null) {
                throw new ValidationException("Department ID cannot be null");
            }
            
            Department department = getDepartmentById(id);
            departmentRepository.delete(department);
        } catch (ValidationException | ResourceNotFoundException e) {
            throw e;
        } catch (DataAccessException e) {
            logger.error("Error deleting department with id: {}", id, e);
            throw new DatabaseException("Failed to delete department", e);
        }
    }
}

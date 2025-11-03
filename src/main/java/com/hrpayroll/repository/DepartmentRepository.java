package com.hrpayroll.repository;

import com.hrpayroll.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Lecture 8: Basic JpaRepository for Department
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    // Find department by name (used for lookups)
    Department findByName(String name);
}

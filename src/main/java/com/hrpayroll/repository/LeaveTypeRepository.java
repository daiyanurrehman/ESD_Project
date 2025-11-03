package com.hrpayroll.repository;

import com.hrpayroll.entity.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Lecture 8: Basic JpaRepository for LeaveType
@Repository
public interface LeaveTypeRepository extends JpaRepository<LeaveType, Long> {
    // Find a leave type by its name (e.g., "Annual")
    LeaveType findByName(String name);
}

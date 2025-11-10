package com.hrpayroll.repository;

import com.hrpayroll.entity.LeaveRequest;
import com.hrpayroll.entity.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Lecture 8: Basic JpaRepository for LeaveRequest
@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    // Find all requests by employee ID
    List<LeaveRequest> findByEmployee_Id(Long employeeId);
    
    // Find all pending requests
    List<LeaveRequest> findByStatus(LeaveStatus status);
}

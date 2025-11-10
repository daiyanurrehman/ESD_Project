package com.hrpayroll.repository;

import com.hrpayroll.entity.PaySlip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Lecture 8: Simple JpaRepository for PaySlip entity
@Repository
public interface PaySlipRepository extends JpaRepository<PaySlip, Long> {
    // Finds all payslips for a specific employee
    List<PaySlip> findByEmployee_Id(Long employeeId);
}

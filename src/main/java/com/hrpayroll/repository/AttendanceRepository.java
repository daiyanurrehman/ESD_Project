package com.hrpayroll.repository;

import com.hrpayroll.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

// Lecture 8: Basic JpaRepository for Attendance
@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    // Find all attendance records for a specific employee on a specific date range
    List<Attendance> findByEmployee_IdAndWorkDateBetween(Long employeeId, LocalDate startDate, LocalDate endDate);
}

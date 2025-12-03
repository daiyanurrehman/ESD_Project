package com.hrpayroll.service;

import com.hrpayroll.entity.Attendance;
import com.hrpayroll.exception.DatabaseException;
import com.hrpayroll.exception.ResourceNotFoundException;
import com.hrpayroll.exception.ValidationException;
import com.hrpayroll.repository.AttendanceRepository;
import com.hrpayroll.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

// Service for managing Attendance
@Service
@SuppressWarnings("null")
public class AttendanceService {

    private static final Logger logger = LoggerFactory.getLogger(AttendanceService.class);
    
    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public AttendanceService(AttendanceRepository attendanceRepository, EmployeeRepository employeeRepository) {
        this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
    }

    // Lecture 10: Atomic record keeping
    @Transactional
    public Attendance recordCheckIn(Long employeeId, Attendance attendance) {
        try {
            if (employeeId == null) {
                throw new ValidationException("Employee ID cannot be null");
            }
            if (attendance == null) {
                throw new ValidationException("Attendance record cannot be null");
            }
            
            employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));

            // In a real system, you'd calculate hours worked here upon check-out
            attendance.setWorkDate(LocalDate.now());
            attendance.setHoursWorked(0.0); // Placeholder until checkout
            
            try {
                return attendanceRepository.save(attendance);
            } catch (DataAccessException e) {
                logger.error("Error saving attendance record", e);
                throw new DatabaseException("Failed to save attendance record", e);
            }
        } catch (ResourceNotFoundException | ValidationException | DatabaseException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error recording check-in", e);
            throw new DatabaseException("An unexpected error occurred while recording check-in", e);
        }
    }

    // Lecture 10: Read operation
    @Transactional(readOnly = true)
    public List<Attendance> getAttendanceByEmployeeAndDateRange(Long employeeId, LocalDate start, LocalDate end) {
        try {
            if (employeeId == null) {
                throw new ValidationException("Employee ID cannot be null");
            }
            if (start == null) {
                throw new ValidationException("Start date cannot be null");
            }
            if (end == null) {
                throw new ValidationException("End date cannot be null");
            }
            if (start.isAfter(end)) {
                throw new ValidationException("Start date cannot be after end date");
            }
            return attendanceRepository.findByEmployee_IdAndWorkDateBetween(employeeId, start, end);
        } catch (ValidationException e) {
            throw e;
        } catch (DataAccessException e) {
            logger.error("Error retrieving attendance records", e);
            throw new DatabaseException("Failed to retrieve attendance records", e);
        } catch (Exception e) {
            logger.error("Unexpected error retrieving attendance records", e);
            throw new DatabaseException("An unexpected error occurred while retrieving attendance records", e);
        }
    }
}

package com.hrpayroll.service;

import com.hrpayroll.entity.Attendance;
import com.hrpayroll.repository.AttendanceRepository;
import com.hrpayroll.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AttendanceService {

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
        employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found."));
        
        // In a real system, you'd calculate hours worked here upon check-out
        attendance.setWorkDate(LocalDate.now());
        attendance.setHoursWorked(0.0); // Placeholder until checkout
        return attendanceRepository.save(attendance);
    }
    
    // Lecture 10: Read operation
    @Transactional(readOnly = true)
    public List<Attendance> getAttendanceByEmployeeAndDateRange(Long employeeId, LocalDate start, LocalDate end) {
        return attendanceRepository.findByEmployee_IdAndWorkDateBetween(employeeId, start, end);
    }
}

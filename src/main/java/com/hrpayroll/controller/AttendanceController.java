package com.hrpayroll.controller;

import com.hrpayroll.entity.Attendance;
import com.hrpayroll.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Autowired
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/employee/{employeeId}/check-in")
    public ResponseEntity<Attendance> recordCheckIn(
            @PathVariable Long employeeId,
            @RequestBody Attendance attendance) {
        Attendance recorded = attendanceService.recordCheckIn(employeeId, attendance);
        return ResponseEntity.status(HttpStatus.CREATED).body(recorded);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Attendance>> getAttendanceByDateRange(
            @PathVariable Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Attendance> attendanceRecords = attendanceService.getAttendanceByEmployeeAndDateRange(
                employeeId, startDate, endDate);
        return ResponseEntity.ok(attendanceRecords);
    }
}

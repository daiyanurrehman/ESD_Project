package com.hrpayroll.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

// Lecture 6-7: @Entity
@Entity
@Table(name = "attendance")
@Data
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
    
    private LocalDate workDate;
    private String checkInTime;
    private String checkOutTime;
    private Double hoursWorked; // Calculated
}

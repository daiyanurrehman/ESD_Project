package com.hrpayroll.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

// Lecture 6-7: @Entity
@Entity
@Table(name = "leave_requests")
@Data
public class LeaveRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
    
    private LocalDate startDate;
    private LocalDate endDate;
    private int durationDays;
    private String reason;
    
    @Enumerated(EnumType.STRING)
    private LeaveStatus status;
    
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;
}

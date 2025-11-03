package com.hrpayroll.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

// Lecture 2: Inheritance from UserAccount, Lecture 6-7: @Entity, @Table
@Entity
@Table(name = "employees")
@Data
@EqualsAndHashCode(callSuper = true)
public class Employee extends UserAccount { 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    private String firstName;
    private String lastName;
    
    private LocalDate dateOfBirth;
    private LocalDate hireDate;
    
    // Lecture 6-7: @ManyToOne Relationship
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    // Lecture 6-7: @ManyToOne Relationship
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "job_title_id", nullable = false)
    private JobTitle jobTitle;
    
    // Lecture 6-7: @OneToMany Relationship
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<LeaveRequest> leaveRequests;
    
    // Lecture 6-7: @OneToMany Relationship
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<PaySlip> paySlips;
    
    // Lecture 6-7: @OneToMany Relationship
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<Attendance> attendanceRecords;
}

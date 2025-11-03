package com.hrpayroll.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

// Lecture 6-7: @Entity
@Entity
@Table(name = "leave_types")
@Data
public class LeaveType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String name; // e.g., Annual, Sick, Maternity
    private int defaultDays; // Standard allowance
    
    @OneToMany(mappedBy = "leaveType")
    private List<LeaveRequest> requests;
}

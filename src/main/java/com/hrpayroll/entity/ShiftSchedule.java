package com.hrpayroll.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

// Lecture 6-7: @Entity
@Entity
@Table(name = "shift_schedule")
@Data
public class ShiftSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name; // Day Shift, Night Shift
    private String startTime;
    private String endTime;
    
    @OneToMany(mappedBy = "shift")
    private List<EmployeeShift> employeeShifts;
}

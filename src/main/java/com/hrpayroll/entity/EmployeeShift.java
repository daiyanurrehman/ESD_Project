package com.hrpayroll.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

// Lecture 6-7: @Entity (M:M Join Entity)
@Entity
@Table(name = "employee_shift")
@Data
public class EmployeeShift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "shift_id")
    private ShiftSchedule shift;

    private LocalDate effectiveDate;
}

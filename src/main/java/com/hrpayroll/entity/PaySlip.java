package com.hrpayroll.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

// Lecture 6-7: @Entity
@Entity
@Table(name = "pay_slips")
@Data
public class PaySlip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
    
    private LocalDate payDate;
    private double grossSalary;
    private double totalDeductions;
    private double netSalary; // Calculated in the Service layer
    
    // One PaySlip contains many Line Items
    @OneToMany(mappedBy = "paySlip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaySlipItem> lineItems;
}

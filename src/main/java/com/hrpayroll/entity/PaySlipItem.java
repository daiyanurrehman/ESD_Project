package com.hrpayroll.entity;

import jakarta.persistence.*;
import lombok.Data;

// Lecture 6-7: @Entity
@Entity
@Table(name = "pay_slip_items")
@Data
public class PaySlipItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private double amount;
    
    @Enumerated(EnumType.STRING)
    private ItemType type;
    
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "pay_slip_id", nullable = false)
    private PaySlip paySlip;
}

package com.hrpayroll.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

// Lecture 6-7: @Entity
@Entity
@Table(name = "expense_claims")
@Data
public class ExpenseClaim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private LocalDate claimDate;
    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    private LeaveStatus status; // Reusing LeaveStatus enum for Claim status

    // One claim has many items
    @OneToMany(mappedBy = "claim", cascade = CascadeType.ALL)
    private List<ExpenseItem> items;
}

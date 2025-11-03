package com.hrpayroll.entity;

import jakarta.persistence.*;
import lombok.Data;

// Lecture 6-7: @Entity
@Entity
@Table(name = "expense_categories")
@Data
public class ExpenseCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name; // e.g., Travel, Food, Software License
    private Double budgetLimit;
}

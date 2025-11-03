package com.hrpayroll.entity;

import jakarta.persistence.*;
import lombok.Data;

// Lecture 6-7: @Entity
@Entity
@Table(name = "expense_items")
@Data
public class ExpenseItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "claim_id")
    private ExpenseClaim claim;

    private String description;
    private Double amount;
    
    // Many items can be of one category
    @ManyToOne
    @JoinColumn(name = "category_id")
    private ExpenseCategory category;
}

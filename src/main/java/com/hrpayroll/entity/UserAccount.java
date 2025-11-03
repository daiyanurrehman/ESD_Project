package com.hrpayroll.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

// Lecture 2: OOP Inheritance - Base class
@Entity
@Table(name = "user_accounts")
@Inheritance(strategy = InheritanceType.JOINED) // Use JOINED strategy for clean tables
@Data
@EqualsAndHashCode
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Account ID

    @Column(unique = true, nullable = false)
    private String username;
    
    // Encapsulation of sensitive data
    private String passwordHash; 
    
    // @Enumerated(EnumType.STRING) (Lecture 6-7: Enumerated values)
    @Enumerated(EnumType.STRING)
    private UserRole role; 
}

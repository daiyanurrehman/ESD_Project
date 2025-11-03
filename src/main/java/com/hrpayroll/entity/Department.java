package com.hrpayroll.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

// Lecture 6-7: @Entity
@Entity
@Table(name = "departments")
@Data
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String name;
    private String description;
    
    // @OneToMany on the 'one' side, mappedBy ensures bidirectional mapping
    @OneToMany(mappedBy = "department")
    private List<Employee> employees;
}

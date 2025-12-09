package com.hrpayroll.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

// Lecture 6-7: @Entity
@Entity
@Table(name = "job_titles")
@Data
public class JobTitle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String title;
    private Double baseSalary; // Base for payroll calculation
    
    @OneToMany(mappedBy = "jobTitle")
    @JsonIgnore // prevent recursive serialization
    private List<Employee> employees;
}

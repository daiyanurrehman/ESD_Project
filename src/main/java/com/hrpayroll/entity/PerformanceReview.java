package com.hrpayroll.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

// Lecture 6-7: @Entity
@Entity
@Table(name = "performance_reviews")
@Data
public class PerformanceReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private LocalDate reviewDate;
    private Double score;
    private String comments;

    // One review has many goals linked
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewGoal> goals;
}

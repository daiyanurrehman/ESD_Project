package com.hrpayroll.entity;

import jakarta.persistence.*;
import lombok.Data;

// Lecture 6-7: @Entity
@Entity
@Table(name = "review_goals")
@Data
public class ReviewGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private PerformanceReview review;

    private String goalDescription;
    private Boolean isAchieved;
}

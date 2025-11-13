package com.hrpayroll.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// DTO for Performance Review API responses
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceReviewDTO {
    private Long id;
    private String employeeName;
    private String reviewerName;
    private Integer rating;
    private String comments;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate reviewDate;
    
    private String department;
}

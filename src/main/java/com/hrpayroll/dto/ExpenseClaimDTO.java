package com.hrpayroll.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

// DTO for Expense Claim API responses
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseClaimDTO {
    private Long id;
    private String employeeName;
    private String description;
    private BigDecimal totalAmount;
    private String status;
    private String category;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate submissionDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate approvalDate;
    
    private String approverName;
}

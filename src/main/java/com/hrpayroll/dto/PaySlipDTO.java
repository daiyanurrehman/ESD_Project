package com.hrpayroll.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaySlipDTO {
    private Long id;
    private Long employeeId;
    private String employeeName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate payDate;
    private double grossSalary;
    private double totalDeductions;
    private double netSalary;
    private List<PaySlipItemDTO> items;
}



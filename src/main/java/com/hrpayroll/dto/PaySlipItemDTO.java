package com.hrpayroll.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaySlipItemDTO {
    private String description;
    private double amount;
    private String type;
}



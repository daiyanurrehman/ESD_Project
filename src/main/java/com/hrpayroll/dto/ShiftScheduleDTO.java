package com.hrpayroll.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO for Shift Schedule API responses
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShiftScheduleDTO {
    private Long id;
    private String name;
    private String startTime;
    private String endTime;
    private String description;
}

package com.hrpayroll.dto;

import lombok.Data;

// Request DTO for signup endpoint
@Data
public class SignupRequest {
    private String username;
    private String password;
}

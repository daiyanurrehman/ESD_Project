package com.hrpayroll.dto;

import lombok.Data;

// Request DTO for login endpoint
@Data
public class LoginRequest {
    private String username;
    private String password;
}

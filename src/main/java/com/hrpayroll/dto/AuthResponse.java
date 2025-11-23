package com.hrpayroll.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

// Response DTO for authentication endpoints
@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String username;
    private String role;
    private long expiresAt;
}

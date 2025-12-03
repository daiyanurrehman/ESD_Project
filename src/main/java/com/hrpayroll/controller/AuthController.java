package com.hrpayroll.controller;

import com.hrpayroll.dto.AuthResponse;
import com.hrpayroll.dto.LoginRequest;
import com.hrpayroll.dto.SignupRequest;
import com.hrpayroll.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// REST controller for authentication endpoints
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // POST /auth/signup - Register new HR user
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest request) {
        // Error handling is done by GlobalExceptionHandler
        AuthResponse response = authService.signup(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // POST /auth/login - Authenticate and get JWT token
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        // Error handling is done by GlobalExceptionHandler
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}

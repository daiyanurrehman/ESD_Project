package com.hrpayroll.service;

import com.hrpayroll.dto.AuthResponse;
import com.hrpayroll.dto.LoginRequest;
import com.hrpayroll.dto.SignupRequest;
import com.hrpayroll.entity.UserAccount;
import com.hrpayroll.entity.UserRole;
import com.hrpayroll.repository.UserAccountRepository;
import com.hrpayroll.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

// Service for authentication operations
@Service
public class AuthService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserAccountRepository userAccountRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil,
            AuthenticationManager authenticationManager) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    // Register new HR user
    public AuthResponse signup(SignupRequest request) {
        // Check if username already exists
        if (userAccountRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // Create new UserAccount with HR_MANAGER role
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername(request.getUsername());
        userAccount.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        userAccount.setRole(UserRole.HR_MANAGER); // HR role only

        userAccountRepository.save(userAccount);

        // Generate JWT token
        String token = jwtUtil.generateToken(userAccount.getUsername(), userAccount.getRole().name());
        long expiresAt = new Date().getTime() + 86400000; // 24 hours from now

        return new AuthResponse(token, userAccount.getUsername(), userAccount.getRole().name(), expiresAt);
    }

    // Authenticate user and generate token
    public AuthResponse login(LoginRequest request) {
        // Authenticate using Spring Security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        // Fetch user details
        UserAccount userAccount = userAccountRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate JWT token
        String token = jwtUtil.generateToken(userAccount.getUsername(), userAccount.getRole().name());
        long expiresAt = new Date().getTime() + 86400000; // 24 hours from now

        return new AuthResponse(token, userAccount.getUsername(), userAccount.getRole().name(), expiresAt);
    }
}

package com.hrpayroll.service;

import com.hrpayroll.dto.AuthResponse;
import com.hrpayroll.dto.LoginRequest;
import com.hrpayroll.dto.SignupRequest;
import com.hrpayroll.entity.UserAccount;
import com.hrpayroll.entity.UserRole;
import com.hrpayroll.exception.AuthenticationException;
import com.hrpayroll.exception.DatabaseException;
import com.hrpayroll.exception.ValidationException;
import com.hrpayroll.repository.UserAccountRepository;
import com.hrpayroll.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

// Service for authentication operations
@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
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
        try {
            // Validate input
            if (request == null) {
                throw new ValidationException("Signup request cannot be null");
            }
            if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
                throw new ValidationException("Username is required");
            }
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                throw new ValidationException("Password is required");
            }
            if (request.getPassword().length() < 6) {
                throw new ValidationException("Password must be at least 6 characters long");
            }
            
            // Check if username already exists
            if (userAccountRepository.findByUsername(request.getUsername()).isPresent()) {
                throw new ValidationException("Username already exists");
            }

            // Create new UserAccount with HR_MANAGER role
            UserAccount userAccount = new UserAccount();
            userAccount.setUsername(request.getUsername());
            userAccount.setPasswordHash(passwordEncoder.encode(request.getPassword()));
            userAccount.setRole(UserRole.HR_MANAGER); // HR role only

            try {
                userAccountRepository.save(userAccount);
            } catch (DataAccessException e) {
                logger.error("Database error while saving user account", e);
                throw new DatabaseException("Failed to create user account", e);
            }

            // Generate JWT token
            try {
                String token = jwtUtil.generateToken(userAccount.getUsername(), userAccount.getRole().name());
                long expiresAt = new Date().getTime() + 86400000; // 24 hours from now

                return new AuthResponse(token, userAccount.getUsername(), userAccount.getRole().name(), expiresAt);
            } catch (Exception e) {
                logger.error("Error generating JWT token", e);
                throw new AuthenticationException("Failed to generate authentication token", e);
            }
        } catch (ValidationException | DatabaseException | AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during signup", e);
            throw new AuthenticationException("An error occurred during signup", e);
        }
    }

    // Authenticate user and generate token
    public AuthResponse login(LoginRequest request) {
        try {
            // Validate input
            if (request == null) {
                throw new ValidationException("Login request cannot be null");
            }
            if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
                throw new ValidationException("Username is required");
            }
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                throw new ValidationException("Password is required");
            }
            
            // Authenticate using Spring Security
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            } catch (BadCredentialsException e) {
                logger.warn("Invalid credentials for user: {}", request.getUsername());
                throw new AuthenticationException("Invalid username or password");
            } catch (Exception e) {
                logger.error("Authentication error", e);
                throw new AuthenticationException("Authentication failed", e);
            }

            // Fetch user details
            UserAccount userAccount = userAccountRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new AuthenticationException("User not found"));

            // Generate JWT token
            try {
                String token = jwtUtil.generateToken(userAccount.getUsername(), userAccount.getRole().name());
                long expiresAt = new Date().getTime() + 86400000; // 24 hours from now

                return new AuthResponse(token, userAccount.getUsername(), userAccount.getRole().name(), expiresAt);
            } catch (Exception e) {
                logger.error("Error generating JWT token", e);
                throw new AuthenticationException("Failed to generate authentication token", e);
            }
        } catch (ValidationException | AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during login", e);
            throw new AuthenticationException("An error occurred during login", e);
        }
    }
}

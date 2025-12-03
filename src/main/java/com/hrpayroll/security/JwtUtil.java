package com.hrpayroll.security;

import com.hrpayroll.config.JwtProperties;
import com.hrpayroll.exception.AuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// Utility class for JWT token generation and validation
@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    
    private final SecretKey secretKey;
    private final long expiration;

    public JwtUtil(JwtProperties jwtProperties) {
        try {
            if (jwtProperties == null || jwtProperties.getSecret() == null || jwtProperties.getSecret().isEmpty()) {
                throw new IllegalArgumentException("JWT secret cannot be null or empty");
            }
            this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
            this.expiration = jwtProperties.getExpiration();
        } catch (Exception e) {
            logger.error("Error initializing JWT utility", e);
            throw new RuntimeException("Failed to initialize JWT utility", e);
        }
    }

    // Generate JWT token from username and role
    public String generateToken(String username, String role) {
        try {
            if (username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("Username cannot be null or empty");
            }
            if (role == null || role.trim().isEmpty()) {
                throw new IllegalArgumentException("Role cannot be null or empty");
            }
            
            Map<String, Object> claims = new HashMap<>();
            claims.put("role", role);
            return createToken(claims, username);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid input for token generation", e);
            throw new AuthenticationException("Invalid input for token generation: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error generating JWT token", e);
            throw new AuthenticationException("Failed to generate JWT token", e);
        }
    }

    private String createToken(Map<String, Object> claims, String subject) {
        try {
            Date now = new Date();
            Date expirationDate = new Date(now.getTime() + expiration);

            return Jwts.builder()
                    .claims(claims)
                    .subject(subject)
                    .issuedAt(now)
                    .expiration(expirationDate)
                    .signWith(secretKey)
                    .compact();
        } catch (Exception e) {
            logger.error("Error creating JWT token", e);
            throw new AuthenticationException("Failed to create JWT token", e);
        }
    }

    // Extract username from token
    public String extractUsername(String token) {
        try {
            if (token == null || token.trim().isEmpty()) {
                throw new IllegalArgumentException("Token cannot be null or empty");
            }
            return extractAllClaims(token).getSubject();
        } catch (ExpiredJwtException e) {
            logger.warn("JWT token has expired");
            throw new AuthenticationException("Token has expired");
        } catch (SignatureException e) {
            logger.warn("Invalid JWT signature");
            throw new AuthenticationException("Invalid token signature");
        } catch (JwtException e) {
            logger.error("Error extracting username from token", e);
            throw new AuthenticationException("Invalid token: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error extracting username from token", e);
            throw new AuthenticationException("Failed to extract username from token", e);
        }
    }

    // Extract role from token
    public String extractRole(String token) {
        try {
            if (token == null || token.trim().isEmpty()) {
                throw new IllegalArgumentException("Token cannot be null or empty");
            }
            return extractAllClaims(token).get("role", String.class);
        } catch (ExpiredJwtException e) {
            logger.warn("JWT token has expired");
            throw new AuthenticationException("Token has expired");
        } catch (SignatureException e) {
            logger.warn("Invalid JWT signature");
            throw new AuthenticationException("Invalid token signature");
        } catch (JwtException e) {
            logger.error("Error extracting role from token", e);
            throw new AuthenticationException("Invalid token: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error extracting role from token", e);
            throw new AuthenticationException("Failed to extract role from token", e);
        }
    }

    // Extract expiration date from token
    public Date extractExpiration(String token) {
        try {
            if (token == null || token.trim().isEmpty()) {
                throw new IllegalArgumentException("Token cannot be null or empty");
            }
            return extractAllClaims(token).getExpiration();
        } catch (ExpiredJwtException e) {
            logger.warn("JWT token has expired");
            throw new AuthenticationException("Token has expired");
        } catch (SignatureException e) {
            logger.warn("Invalid JWT signature");
            throw new AuthenticationException("Invalid token signature");
        } catch (JwtException e) {
            logger.error("Error extracting expiration from token", e);
            throw new AuthenticationException("Invalid token: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error extracting expiration from token", e);
            throw new AuthenticationException("Failed to extract expiration from token", e);
        }
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw e;
        } catch (SignatureException e) {
            throw e;
        } catch (JwtException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error parsing JWT token", e);
            throw new AuthenticationException("Failed to parse token", e);
        }
    }

    // Check if token is expired
    public boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (AuthenticationException e) {
            // If we can't extract expiration, consider it expired
            return true;
        } catch (Exception e) {
            logger.error("Error checking token expiration", e);
            return true;
        }
    }

    // Validate token against username and expiration
    public boolean validateToken(String token, String username) {
        try {
            if (token == null || token.trim().isEmpty()) {
                return false;
            }
            if (username == null || username.trim().isEmpty()) {
                return false;
            }
            
            final String extractedUsername = extractUsername(token);
            return (extractedUsername.equals(username) && !isTokenExpired(token));
        } catch (AuthenticationException e) {
            logger.debug("Token validation failed: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("Error validating token", e);
            return false;
        }
    }
}

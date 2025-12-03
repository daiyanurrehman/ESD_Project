package com.hrpayroll.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;

import jakarta.annotation.PostConstruct; // or javax.annotation.PostConstruct depending on your Jakarta/JavaEE setup

// Configuration class to hold JWT settings from application.properties
@Configuration
@Getter
public class JwtProperties {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    @PostConstruct
    private void validate() {
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("Missing configuration: jwt.secret must be provided");
        }
        if (expiration <= 0) {
            throw new IllegalStateException("Invalid configuration: jwt.expiration must be a positive number");
        }
    }
}

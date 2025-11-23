package com.hrpayroll.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;

// Configuration class to hold JWT settings from application.properties
@Configuration
@Getter
public class JwtProperties {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;
}

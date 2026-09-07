package com.example.orderinventory.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security.jwt")
public record JwtConfig(
        String secret,
        long accessTokenTtlSeconds,
        String issuer
) {
}


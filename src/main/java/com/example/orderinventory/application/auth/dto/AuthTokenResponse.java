package com.example.orderinventory.application.auth.dto;

import java.util.Set;

public record AuthTokenResponse(
        String accessToken,
        String tokenType,
        long expiresInSeconds,
        String username,
        String role,
        Set<String> permissions
) {
}


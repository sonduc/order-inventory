package com.example.orderinventory.application.webhook.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record WebhookRegistrationRequest(
        @NotBlank(message = "Target URL is required.")
        String targetUrl,
        String secret,
        List<String> eventTypes
) {
}


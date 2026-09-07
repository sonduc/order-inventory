package com.example.orderinventory.application.webhook.dto;

import java.time.Instant;

public record WebhookDeliveryLog(
        String targetUrl,
        String eventType,
        boolean successful,
        int attempts,
        Instant attemptedAt
) {
}


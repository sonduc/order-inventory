package com.example.orderinventory.application.webhook.dto;

import java.util.Map;

public record WebhookPayload(
        String eventType,
        String aggregateId,
        Map<String, Object> payload
) {
}


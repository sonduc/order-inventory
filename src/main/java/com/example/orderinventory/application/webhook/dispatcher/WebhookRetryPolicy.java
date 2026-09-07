package com.example.orderinventory.application.webhook.dispatcher;

public record WebhookRetryPolicy(int maxAttempts, long backoffMillis) {

    public WebhookRetryPolicy {
        if (maxAttempts < 1) {
            throw new IllegalArgumentException("maxAttempts must be at least 1.");
        }
        if (backoffMillis < 0) {
            throw new IllegalArgumentException("backoffMillis cannot be negative.");
        }
    }
}


package com.example.orderinventory.domain.payment.event;

import com.example.orderinventory.domain.shared.event.DomainEvent;
import java.time.Instant;

public record PaymentFailedEvent(Long orderId, String reason, Instant occurredAt) implements DomainEvent {

    public PaymentFailedEvent(Long orderId, String reason) {
        this(orderId, reason, Instant.now());
    }

    @Override
    public String aggregateId() {
        return String.valueOf(orderId);
    }
}


package com.example.orderinventory.domain.payment.event;

import com.example.orderinventory.domain.shared.event.DomainEvent;
import java.time.Instant;

public record PaymentRefundedEvent(Long orderId, Instant occurredAt) implements DomainEvent {

    public PaymentRefundedEvent(Long orderId) {
        this(orderId, Instant.now());
    }

    @Override
    public String aggregateId() {
        return String.valueOf(orderId);
    }
}


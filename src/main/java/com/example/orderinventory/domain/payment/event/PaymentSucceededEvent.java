package com.example.orderinventory.domain.payment.event;

import com.example.orderinventory.domain.shared.event.DomainEvent;
import java.time.Instant;

public record PaymentSucceededEvent(Long orderId, String providerReference, Instant occurredAt) implements DomainEvent {

    public PaymentSucceededEvent(Long orderId, String providerReference) {
        this(orderId, providerReference, Instant.now());
    }

    @Override
    public String aggregateId() {
        return String.valueOf(orderId);
    }
}


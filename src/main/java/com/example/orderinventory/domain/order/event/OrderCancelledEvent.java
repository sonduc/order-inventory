package com.example.orderinventory.domain.order.event;

import com.example.orderinventory.domain.shared.event.DomainEvent;
import java.time.Instant;

public record OrderCancelledEvent(Long orderId, Instant occurredAt) implements DomainEvent {

    public OrderCancelledEvent(Long orderId) {
        this(orderId, Instant.now());
    }

    @Override
    public String aggregateId() {
        return String.valueOf(orderId);
    }
}


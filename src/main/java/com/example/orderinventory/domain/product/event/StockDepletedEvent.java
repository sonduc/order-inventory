package com.example.orderinventory.domain.product.event;

import com.example.orderinventory.domain.shared.event.DomainEvent;
import java.time.Instant;

public record StockDepletedEvent(Long productId, Instant occurredAt) implements DomainEvent {

    public StockDepletedEvent(Long productId) {
        this(productId, Instant.now());
    }

    @Override
    public String aggregateId() {
        return String.valueOf(productId);
    }
}


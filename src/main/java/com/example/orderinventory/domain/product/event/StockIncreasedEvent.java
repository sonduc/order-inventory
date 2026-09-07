package com.example.orderinventory.domain.product.event;

import com.example.orderinventory.domain.shared.event.DomainEvent;
import java.time.Instant;

public record StockIncreasedEvent(
        Long productId,
        Integer quantity,
        Integer remainingStock,
        Instant occurredAt
) implements DomainEvent {

    public StockIncreasedEvent(Long productId, Integer quantity, Integer remainingStock) {
        this(productId, quantity, remainingStock, Instant.now());
    }

    @Override
    public String aggregateId() {
        return String.valueOf(productId);
    }
}


package com.example.orderinventory.domain.shared.event;

import java.time.Instant;

public interface DomainEvent {

    String aggregateId();

    Instant occurredAt();

    default String eventName() {
        return getClass().getSimpleName();
    }
}


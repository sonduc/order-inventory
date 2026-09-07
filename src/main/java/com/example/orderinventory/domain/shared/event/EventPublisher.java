package com.example.orderinventory.domain.shared.event;

public interface EventPublisher {

    void publish(DomainEvent event);
}


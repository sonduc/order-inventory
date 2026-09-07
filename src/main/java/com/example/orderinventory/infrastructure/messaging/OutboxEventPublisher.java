package com.example.orderinventory.infrastructure.messaging;

import com.example.orderinventory.domain.shared.event.DomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OutboxEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(OutboxEventPublisher.class);

    public void stage(DomainEvent event) {
        log.debug("Outbox staging placeholder for {}.", event.eventName());
    }
}


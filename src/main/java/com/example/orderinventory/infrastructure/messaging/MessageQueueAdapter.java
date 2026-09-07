package com.example.orderinventory.infrastructure.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MessageQueueAdapter {

    private static final Logger log = LoggerFactory.getLogger(MessageQueueAdapter.class);

    public void publish(String topic, Object payload) {
        log.debug("Message queue placeholder published to topic {} with payload type {}.", topic, payload.getClass().getSimpleName());
    }
}


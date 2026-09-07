package com.example.orderinventory.application.order.listener;

import com.example.orderinventory.domain.order.event.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderWebhookListener {

    private static final Logger log = LoggerFactory.getLogger(OrderWebhookListener.class);

    @EventListener
    public void onOrderCreated(OrderCreatedEvent event) {
        log.info("Webhook dispatch placeholder queued for order {}.", event.orderId());
    }
}


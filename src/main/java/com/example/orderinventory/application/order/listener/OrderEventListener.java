package com.example.orderinventory.application.order.listener;

import com.example.orderinventory.domain.order.event.OrderCancelledEvent;
import com.example.orderinventory.domain.order.event.OrderConfirmedEvent;
import com.example.orderinventory.domain.order.event.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    private static final Logger log = LoggerFactory.getLogger(OrderEventListener.class);

    @EventListener
    public void onOrderCreated(OrderCreatedEvent event) {
        log.info("Order {} created.", event.orderId());
    }

    @EventListener
    public void onOrderConfirmed(OrderConfirmedEvent event) {
        log.info("Order {} confirmed.", event.orderId());
    }

    @EventListener
    public void onOrderCancelled(OrderCancelledEvent event) {
        log.info("Order {} cancelled.", event.orderId());
    }
}


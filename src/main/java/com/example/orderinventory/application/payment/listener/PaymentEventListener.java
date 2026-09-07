package com.example.orderinventory.application.payment.listener;

import com.example.orderinventory.domain.payment.event.PaymentFailedEvent;
import com.example.orderinventory.domain.payment.event.PaymentSucceededEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventListener {

    private static final Logger log = LoggerFactory.getLogger(PaymentEventListener.class);

    @EventListener
    public void onPaymentSucceeded(PaymentSucceededEvent event) {
        log.info("Payment succeeded for order {} with reference {}.", event.orderId(), event.providerReference());
    }

    @EventListener
    public void onPaymentFailed(PaymentFailedEvent event) {
        log.warn("Payment failed for order {}: {}", event.orderId(), event.reason());
    }
}


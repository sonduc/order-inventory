package com.example.orderinventory.domain.payment;

import com.example.orderinventory.domain.order.Order;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MockPaymentGateway {

    private final double failureRate;

    public MockPaymentGateway(@Value("${app.payment.mock.failure-rate:0.2}") double failureRate) {
        this.failureRate = Math.max(0.0, Math.min(1.0, failureRate));
    }

    public PaymentGatewayResult charge(Order order) {
        boolean success = ThreadLocalRandom.current().nextDouble() >= failureRate;
        String providerReference = "MOCK-" + UUID.randomUUID().toString().substring(0, 8);
        String failureReason = success ? null : "Mock gateway rejected payment for order %d.".formatted(order.getId());
        return new PaymentGatewayResult(success, providerReference, failureReason);
    }

    public record PaymentGatewayResult(boolean success, String providerReference, String failureReason) {
    }
}


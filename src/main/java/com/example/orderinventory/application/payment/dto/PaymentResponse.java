package com.example.orderinventory.application.payment.dto;

import com.example.orderinventory.domain.payment.PaymentStatus;
import java.time.Instant;

public record PaymentResponse(
        Long orderId,
        PaymentStatus status,
        Instant attemptedAt,
        String providerReference
) {
}


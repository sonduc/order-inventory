package com.example.orderinventory.application.order.dto;

import com.example.orderinventory.domain.order.OrderStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
        Long id,
        Long customerId,
        OrderStatus status,
        BigDecimal totalAmount,
        Instant createdAt,
        List<OrderItemResponse> items
) {
    public record OrderItemResponse(Long productId, Integer quantity, BigDecimal priceAtOrderTime) {
    }
}


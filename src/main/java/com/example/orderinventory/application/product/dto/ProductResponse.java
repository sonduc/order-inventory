package com.example.orderinventory.application.product.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity,
        Long version,
        Instant createdAt,
        Instant updatedAt
) {
}

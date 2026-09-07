package com.example.orderinventory.application.product.dto;

import com.example.orderinventory.domain.product.InventoryLockMode;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record StockAdjustmentRequest(
        @NotNull(message = "Quantity delta is required.")
        @Positive(message = "Quantity delta must be greater than zero.")
        Integer quantityDelta,

        @NotNull(message = "Decrease flag is required.")
        Boolean decrease,

        @Positive(message = "Expected version must be positive.")
        Long expectedVersion,

        InventoryLockMode lockMode,

        String reason
) {
}

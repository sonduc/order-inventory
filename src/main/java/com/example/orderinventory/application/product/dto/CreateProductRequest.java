package com.example.orderinventory.application.product.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CreateProductRequest(
        @NotBlank(message = "Name is required.")
        String name,

        String description,

        @NotNull(message = "Price is required.")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero.")
        BigDecimal price,

        @NotNull(message = "Stock quantity is required.")
        @Min(value = 0, message = "Stock quantity cannot be negative.")
        Integer stockQuantity
) {
}

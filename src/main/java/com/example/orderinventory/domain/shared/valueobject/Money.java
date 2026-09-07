package com.example.orderinventory.domain.shared.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Money(BigDecimal value) {

    public Money {
        if (value == null || value.signum() < 0) {
            throw new IllegalArgumentException("Money value must be zero or positive.");
        }
        value = value.setScale(2, RoundingMode.HALF_UP);
    }

    public Money add(Money other) {
        return new Money(value.add(other.value));
    }

    public Money multiply(int factor) {
        return new Money(value.multiply(BigDecimal.valueOf(factor)));
    }
}


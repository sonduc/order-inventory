package com.example.orderinventory.domain.shared.valueobject;

public record Quantity(int value) {

    public Quantity {
        if (value < 0) {
            throw new IllegalArgumentException("Quantity must be zero or positive.");
        }
    }

    public Quantity add(int delta) {
        return new Quantity(value + delta);
    }

    public Quantity subtract(int delta) {
        return new Quantity(value - delta);
    }
}


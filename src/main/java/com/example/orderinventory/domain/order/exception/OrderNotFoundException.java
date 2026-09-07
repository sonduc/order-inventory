package com.example.orderinventory.domain.order.exception;

import com.example.orderinventory.domain.shared.exception.BusinessException;
import com.example.orderinventory.domain.shared.exception.ErrorCode;

public class OrderNotFoundException extends BusinessException {

    public OrderNotFoundException(Long orderId) {
        super(ErrorCode.ORDER_NOT_FOUND, "Order %d was not found.".formatted(orderId));
    }
}


package com.example.orderinventory.domain.order.exception;

import com.example.orderinventory.domain.shared.exception.BusinessException;
import com.example.orderinventory.domain.shared.exception.ErrorCode;

public class InvalidOrderStateException extends BusinessException {

    public InvalidOrderStateException(String message) {
        super(ErrorCode.INVALID_ORDER_STATE, message);
    }
}


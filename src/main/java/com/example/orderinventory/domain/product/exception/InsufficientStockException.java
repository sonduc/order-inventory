package com.example.orderinventory.domain.product.exception;

import com.example.orderinventory.domain.shared.exception.BusinessException;
import com.example.orderinventory.domain.shared.exception.ErrorCode;

public class InsufficientStockException extends BusinessException {

    public InsufficientStockException(String message) {
        super(ErrorCode.INSUFFICIENT_STOCK, message);
    }
}

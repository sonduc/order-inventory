package com.example.orderinventory.domain.payment.exception;

import com.example.orderinventory.domain.shared.exception.BusinessException;
import com.example.orderinventory.domain.shared.exception.ErrorCode;

public class PaymentException extends BusinessException {

    public PaymentException(String message) {
        super(ErrorCode.PAYMENT_FAILED, message);
    }
}


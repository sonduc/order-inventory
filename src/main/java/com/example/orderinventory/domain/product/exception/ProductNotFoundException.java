package com.example.orderinventory.domain.product.exception;

import com.example.orderinventory.domain.shared.exception.BusinessException;
import com.example.orderinventory.domain.shared.exception.ErrorCode;

public class ProductNotFoundException extends BusinessException {

    public ProductNotFoundException(Long productId) {
        super(ErrorCode.PRODUCT_NOT_FOUND, "Product %d was not found.".formatted(productId));
    }
}

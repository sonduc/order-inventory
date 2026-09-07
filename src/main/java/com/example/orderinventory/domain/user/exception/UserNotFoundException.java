package com.example.orderinventory.domain.user.exception;

import com.example.orderinventory.domain.shared.exception.BusinessException;
import com.example.orderinventory.domain.shared.exception.ErrorCode;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException(Long userId) {
        super(ErrorCode.USER_NOT_FOUND, "User %d was not found.".formatted(userId));
    }
}


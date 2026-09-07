package com.example.orderinventory.application.order.dto;

import com.example.orderinventory.domain.order.OrderStatus;
import java.time.Instant;

public record OrderSearchCriteria(
        OrderStatus status,
        Long customerId,
        Instant fromTime,
        Instant toTime
) {
}


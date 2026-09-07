package com.example.orderinventory.application.order.mapper;

import com.example.orderinventory.application.order.dto.OrderResponse;
import com.example.orderinventory.domain.order.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getCustomerId(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                order.getItems().stream()
                        .map(item -> new OrderResponse.OrderItemResponse(
                                item.getProduct().getId(),
                                item.getQuantity(),
                                item.getPriceAtOrderTime()
                        ))
                        .toList()
        );
    }
}


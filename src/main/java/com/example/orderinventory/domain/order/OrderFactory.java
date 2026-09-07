package com.example.orderinventory.domain.order;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class OrderFactory {

    public Order createPendingOrder(Long customerId, List<OrderItem> items) {
        Order order = new Order();
        order.setCustomerId(customerId);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(calculateTotal(items));
        items.forEach(order::addItem);
        return order;
    }

    private BigDecimal calculateTotal(List<OrderItem> items) {
        return items.stream()
                .map(item -> item.getPriceAtOrderTime().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}


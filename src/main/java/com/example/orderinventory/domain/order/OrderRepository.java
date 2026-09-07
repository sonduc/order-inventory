package com.example.orderinventory.domain.order;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Optional<Order> findById(Long orderId);

    Optional<Order> findDetailsById(Long orderId);

    List<Order> search(OrderStatus status, Long customerId, Instant fromTime, Instant toTime);

    Order save(Order order);
}

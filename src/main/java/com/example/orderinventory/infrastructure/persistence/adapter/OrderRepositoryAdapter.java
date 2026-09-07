package com.example.orderinventory.infrastructure.persistence.adapter;

import com.example.orderinventory.domain.order.Order;
import com.example.orderinventory.domain.order.OrderRepository;
import com.example.orderinventory.domain.order.OrderStatus;
import com.example.orderinventory.infrastructure.persistence.jpa.OrderJpaRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryAdapter implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    public OrderRepositoryAdapter(OrderJpaRepository orderJpaRepository) {
        this.orderJpaRepository = orderJpaRepository;
    }

    @Override
    public Optional<Order> findById(Long orderId) {
        return orderJpaRepository.findById(orderId);
    }

    @Override
    public Optional<Order> findDetailsById(Long orderId) {
        return orderJpaRepository.findDetailsById(orderId);
    }

    @Override
    public List<Order> search(OrderStatus status, Long customerId, Instant fromTime, Instant toTime) {
        return orderJpaRepository.search(status, customerId, fromTime, toTime);
    }

    @Override
    public Order save(Order order) {
        return orderJpaRepository.save(order);
    }
}


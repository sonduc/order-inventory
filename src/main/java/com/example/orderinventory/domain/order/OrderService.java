package com.example.orderinventory.domain.order;

import com.example.orderinventory.domain.order.event.OrderCancelledEvent;
import com.example.orderinventory.domain.order.event.OrderConfirmedEvent;
import com.example.orderinventory.domain.order.event.OrderCreatedEvent;
import com.example.orderinventory.domain.order.event.OrderShippedEvent;
import com.example.orderinventory.domain.order.exception.OrderNotFoundException;
import com.example.orderinventory.domain.shared.event.EventPublisher;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final EventPublisher eventPublisher;

    public OrderService(OrderRepository orderRepository, EventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Order create(Order order) {
        Order savedOrder = orderRepository.save(order);
        eventPublisher.publish(new OrderCreatedEvent(savedOrder.getId()));
        return savedOrder;
    }

    @Transactional(readOnly = true)
    public Order getRequiredOrder(Long orderId) {
        return orderRepository.findDetailsById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    @Transactional(readOnly = true)
    public List<Order> search(OrderStatus status, Long customerId, Instant fromTime, Instant toTime) {
        return orderRepository.search(status, customerId, fromTime, toTime);
    }

    @Transactional
    public Order confirm(Long orderId) {
        Order order = getRequiredOrder(orderId);
        order.confirm();
        Order savedOrder = orderRepository.save(order);
        eventPublisher.publish(new OrderConfirmedEvent(savedOrder.getId()));
        return savedOrder;
    }

    @Transactional
    public Order cancel(Long orderId) {
        Order order = getRequiredOrder(orderId);
        order.cancel();
        Order savedOrder = orderRepository.save(order);
        eventPublisher.publish(new OrderCancelledEvent(savedOrder.getId()));
        return savedOrder;
    }

    @Transactional
    public Order ship(Long orderId) {
        Order order = getRequiredOrder(orderId);
        order.ship();
        Order savedOrder = orderRepository.save(order);
        eventPublisher.publish(new OrderShippedEvent(savedOrder.getId()));
        return savedOrder;
    }
}


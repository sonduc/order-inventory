package com.example.orderinventory.presentation.rest;

import com.example.orderinventory.application.order.OrderApplicationService;
import com.example.orderinventory.application.order.dto.CreateOrderRequest;
import com.example.orderinventory.application.order.dto.OrderResponse;
import com.example.orderinventory.application.order.dto.OrderSearchCriteria;
import com.example.orderinventory.domain.order.OrderStatus;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderApplicationService orderApplicationService;

    public OrderController(OrderApplicationService orderApplicationService) {
        this.orderApplicationService = orderApplicationService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ORDER_WRITE')")
    public OrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request) {
        return orderApplicationService.createOrder(request);
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasAuthority('ORDER_READ')")
    public OrderResponse getOrder(@PathVariable Long orderId) {
        return orderApplicationService.getOrder(orderId);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ORDER_READ')")
    public List<OrderResponse> searchOrders(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fromTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant toTime
    ) {
        return orderApplicationService.searchOrders(new OrderSearchCriteria(status, customerId, fromTime, toTime));
    }

    @PostMapping("/{orderId}/confirm")
    @PreAuthorize("hasAuthority('ORDER_WRITE')")
    public OrderResponse confirmOrder(@PathVariable Long orderId) {
        return orderApplicationService.confirmOrder(orderId);
    }

    @PostMapping("/{orderId}/cancel")
    @PreAuthorize("hasAuthority('ORDER_WRITE')")
    public OrderResponse cancelOrder(@PathVariable Long orderId) {
        return orderApplicationService.cancelOrder(orderId);
    }

    @PostMapping("/{orderId}/ship")
    @PreAuthorize("hasAuthority('ORDER_WRITE')")
    public OrderResponse shipOrder(@PathVariable Long orderId) {
        return orderApplicationService.shipOrder(orderId);
    }
}

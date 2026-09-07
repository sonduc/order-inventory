package com.example.orderinventory.application.order;

import com.example.orderinventory.application.order.dto.CreateOrderRequest;
import com.example.orderinventory.application.order.dto.OrderResponse;
import com.example.orderinventory.application.order.dto.OrderSearchCriteria;
import com.example.orderinventory.application.order.mapper.OrderMapper;
import com.example.orderinventory.domain.order.Order;
import com.example.orderinventory.domain.order.OrderFactory;
import com.example.orderinventory.domain.order.OrderItem;
import com.example.orderinventory.domain.order.OrderService;
import com.example.orderinventory.domain.product.InventoryLockMode;
import com.example.orderinventory.domain.product.ProductService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderApplicationService {

    private final OrderService orderService;
    private final OrderFactory orderFactory;
    private final ProductService productService;
    private final OrderMapper orderMapper;

    public OrderApplicationService(
            OrderService orderService,
            OrderFactory orderFactory,
            ProductService productService,
            OrderMapper orderMapper
    ) {
        this.orderService = orderService;
        this.orderFactory = orderFactory;
        this.productService = productService;
        this.orderMapper = orderMapper;
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        List<OrderItem> items = request.items().stream()
                .map(itemRequest -> {
                    var product = productService.getRequiredProduct(itemRequest.productId());
                    OrderItem item = new OrderItem();
                    item.setProduct(product);
                    item.setQuantity(itemRequest.quantity());
                    item.setPriceAtOrderTime(product.getPrice());
                    return item;
                })
                .toList();

        Order order = orderFactory.createPendingOrder(request.customerId(), items);
        Order savedOrder = orderService.create(order);

        request.items().forEach(itemRequest -> productService.adjustStock(
                itemRequest.productId(),
                itemRequest.quantity(),
                true,
                null,
                InventoryLockMode.PESSIMISTIC,
                "Reserved for order %d".formatted(savedOrder.getId())
        ));

        return orderMapper.toResponse(orderService.getRequiredOrder(savedOrder.getId()));
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long orderId) {
        return orderMapper.toResponse(orderService.getRequiredOrder(orderId));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> searchOrders(OrderSearchCriteria criteria) {
        return orderService.search(criteria.status(), criteria.customerId(), criteria.fromTime(), criteria.toTime())
                .stream()
                .map(orderMapper::toResponse)
                .toList();
    }

    @Transactional
    public OrderResponse confirmOrder(Long orderId) {
        return orderMapper.toResponse(orderService.confirm(orderId));
    }

    @Transactional
    public OrderResponse cancelOrder(Long orderId) {
        return orderMapper.toResponse(orderService.cancel(orderId));
    }

    @Transactional
    public OrderResponse shipOrder(Long orderId) {
        return orderMapper.toResponse(orderService.ship(orderId));
    }
}

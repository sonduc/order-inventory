package com.example.orderinventory.presentation.console;

import com.example.orderinventory.application.order.OrderApplicationService;
import com.example.orderinventory.application.order.dto.OrderSearchCriteria;
import org.springframework.stereotype.Component;

@Component
public class OrderManagementCommand implements ConsoleCommand {

    private final OrderApplicationService orderApplicationService;

    public OrderManagementCommand(OrderApplicationService orderApplicationService) {
        this.orderApplicationService = orderApplicationService;
    }

    @Override
    public String name() {
        return "order:list";
    }

    @Override
    public String description() {
        return "List recent orders.";
    }

    @Override
    public String execute(java.util.List<String> arguments) {
        return orderApplicationService.searchOrders(new OrderSearchCriteria(null, null, null, null)).stream()
                .map(order -> "%d:%s(total=%s)".formatted(order.id(), order.status(), order.totalAmount()))
                .collect(java.util.stream.Collectors.joining(", "));
    }
}

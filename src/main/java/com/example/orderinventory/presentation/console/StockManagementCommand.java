package com.example.orderinventory.presentation.console;

import com.example.orderinventory.application.product.ProductApplicationService;
import org.springframework.stereotype.Component;

@Component
public class StockManagementCommand implements ConsoleCommand {

    private final ProductApplicationService productApplicationService;

    public StockManagementCommand(ProductApplicationService productApplicationService) {
        this.productApplicationService = productApplicationService;
    }

    @Override
    public String name() {
        return "stock:list";
    }

    @Override
    public String description() {
        return "List current products with stock quantities.";
    }

    @Override
    public String execute(java.util.List<String> arguments) {
        return productApplicationService.listProducts().stream()
                .map(product -> "%d:%s(stock=%d)".formatted(
                        product.id(),
                        product.name(),
                        product.stockQuantity()
                ))
                .collect(java.util.stream.Collectors.joining(", "));
    }
}

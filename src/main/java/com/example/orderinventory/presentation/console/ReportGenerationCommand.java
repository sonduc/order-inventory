package com.example.orderinventory.presentation.console;

import com.example.orderinventory.application.product.ProductApplicationService;
import org.springframework.stereotype.Component;

@Component
public class ReportGenerationCommand implements ConsoleCommand {

    private final ProductApplicationService productApplicationService;

    public ReportGenerationCommand(ProductApplicationService productApplicationService) {
        this.productApplicationService = productApplicationService;
    }

    @Override
    public String name() {
        return "report:stock-summary";
    }

    @Override
    public String description() {
        return "Generate a simple stock summary.";
    }

    @Override
    public String execute(java.util.List<String> arguments) {
        long productCount = productApplicationService.listProducts().size();
        return "Stock summary generated for %d product(s).".formatted(productCount);
    }
}

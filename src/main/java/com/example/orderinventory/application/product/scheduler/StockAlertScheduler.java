package com.example.orderinventory.application.product.scheduler;

import com.example.orderinventory.domain.product.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "app.scheduling.stock-alert", name = "enabled", havingValue = "true")
public class StockAlertScheduler {

    private static final Logger log = LoggerFactory.getLogger(StockAlertScheduler.class);

    private final ProductRepository productRepository;

    public StockAlertScheduler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Scheduled(cron = "${app.scheduling.stock-alert.cron:0 0/30 * * * *}", zone = "UTC")
    public void publishLowStockSummary() {
        long lowStockCount = productRepository.findAll().stream()
                .filter(product -> product.getStockQuantity() <= 5)
                .count();
        log.info("Low-stock scan completed. {} product(s) are at or below threshold.", lowStockCount);
    }
}

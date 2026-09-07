package com.example.orderinventory.application.product.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "app.scheduling.inventory-sync", name = "enabled", havingValue = "true")
public class InventorySyncScheduler {

    private static final Logger log = LoggerFactory.getLogger(InventorySyncScheduler.class);

    @Scheduled(fixedDelayString = "${app.scheduling.inventory-sync.fixed-delay-ms:900000}")
    public void synchronizeInventory() {
        log.info("Inventory sync scheduler placeholder executed.");
    }
}


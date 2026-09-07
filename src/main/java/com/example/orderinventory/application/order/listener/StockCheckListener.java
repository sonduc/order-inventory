package com.example.orderinventory.application.order.listener;

import com.example.orderinventory.domain.product.event.StockDecreasedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StockCheckListener {

    private static final Logger log = LoggerFactory.getLogger(StockCheckListener.class);

    @EventListener
    public void onStockDecreased(StockDecreasedEvent event) {
        log.info("Stock decreased for product {} by {}. Remaining stock: {}.",
                event.productId(),
                event.quantity(),
                event.remainingStock());
    }
}


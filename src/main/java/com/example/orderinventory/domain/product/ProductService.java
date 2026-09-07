package com.example.orderinventory.domain.product;

import com.example.orderinventory.domain.product.event.StockDecreasedEvent;
import com.example.orderinventory.domain.product.event.StockDepletedEvent;
import com.example.orderinventory.domain.product.event.StockIncreasedEvent;
import com.example.orderinventory.domain.product.exception.InsufficientStockException;
import com.example.orderinventory.domain.product.exception.ProductNotFoundException;
import com.example.orderinventory.domain.shared.event.EventPublisher;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final EventPublisher eventPublisher;

    public ProductService(ProductRepository productRepository, EventPublisher eventPublisher) {
        this.productRepository = productRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional(readOnly = true)
    public List<Product> listProducts() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product getRequiredProduct(Long productId) {
        return findProduct(productId);
    }

    @Transactional
    public Product createProduct(String name, String description, BigDecimal price, Integer stockQuantity) {
        Product product = new Product();
        applyProductValues(product, name, description, price, stockQuantity);
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long productId, String name, String description, BigDecimal price, Integer stockQuantity) {
        Product product = findProduct(productId);
        applyProductValues(product, name, description, price, stockQuantity);
        return productRepository.save(product);
    }

    @Transactional
    public Product adjustStock(
            Long productId,
            Integer quantityDelta,
            Boolean decrease,
            Long expectedVersion,
            InventoryLockMode lockMode,
            String reason
    ) {
        Product product = lockMode == InventoryLockMode.PESSIMISTIC
                ? findProductForUpdate(productId)
                : findProduct(productId);

        if (lockMode != InventoryLockMode.PESSIMISTIC
                && expectedVersion != null
                && !expectedVersion.equals(product.getVersion())) {
            throw new ObjectOptimisticLockingFailureException(Product.class, productId);
        }

        int currentStock = product.getStockQuantity();
        int delta = quantityDelta;
        int updatedStock = Boolean.TRUE.equals(decrease) ? currentStock - delta : currentStock + delta;

        if (updatedStock < 0) {
            throw new InsufficientStockException(
                    "Cannot decrease stock below zero for product %d.".formatted(productId)
            );
        }

        product.setStockQuantity(updatedStock);
        Product savedProduct = productRepository.save(product);
        productRepository.saveInventoryLog(new InventoryLog(productId, Boolean.TRUE.equals(decrease) ? -delta : delta, updatedStock, reason));

        if (Boolean.TRUE.equals(decrease)) {
            eventPublisher.publish(new StockDecreasedEvent(productId, delta, updatedStock));
            if (updatedStock == 0) {
                eventPublisher.publish(new StockDepletedEvent(productId));
            }
        } else {
            eventPublisher.publish(new StockIncreasedEvent(productId, delta, updatedStock));
        }

        return savedProduct;
    }

    private Product findProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    private Product findProductForUpdate(Long productId) {
        return productRepository.findByIdForUpdate(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    private void applyProductValues(
            Product product,
            String name,
            String description,
            BigDecimal price,
            Integer stockQuantity
    ) {
        product.setName(name.trim());
        product.setDescription(description);
        product.setPrice(price);
        product.setStockQuantity(stockQuantity);
    }
}

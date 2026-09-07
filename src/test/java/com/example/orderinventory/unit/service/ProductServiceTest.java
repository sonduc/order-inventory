package com.example.orderinventory.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.orderinventory.domain.product.InventoryLog;
import com.example.orderinventory.domain.product.InventoryLockMode;
import com.example.orderinventory.domain.product.Product;
import com.example.orderinventory.domain.product.ProductRepository;
import com.example.orderinventory.domain.product.ProductService;
import com.example.orderinventory.domain.product.exception.InsufficientStockException;
import com.example.orderinventory.domain.shared.event.EventPublisher;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private EventPublisher eventPublisher;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository, eventPublisher);
    }

    @Test
    void createProductShouldPersistRequestValues() {
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product product = invocation.getArgument(0);
            product.setId(1L);
            product.setVersion(0L);
            return product;
        });

        var product = productService.createProduct("Laptop", "14 inch developer laptop", new BigDecimal("1500.00"), 5);
        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);

        verify(productRepository).save(captor.capture());

        assertThat(captor.getValue().getName()).isEqualTo("Laptop");
        assertThat(captor.getValue().getStockQuantity()).isEqualTo(5);
        assertThat(product.getId()).isEqualTo(1L);
        assertThat(product.getPrice()).isEqualByComparingTo("1500.00");
    }

    @Test
    void adjustStockShouldRejectNegativeInventory() {
        Product product = new Product();
        product.setId(99L);
        product.setName("Mouse");
        product.setPrice(new BigDecimal("19.99"));
        product.setStockQuantity(1);
        product.setVersion(2L);

        when(productRepository.findById(99L)).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> productService.adjustStock(
                99L,
                2,
                true,
                2L,
                InventoryLockMode.OPTIMISTIC,
                "simulate oversell"
        ))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessageContaining("Cannot decrease stock below zero");
    }

    @Test
    void adjustStockShouldPersistInventoryLog() {
        Product product = new Product();
        product.setId(10L);
        product.setName("Headset");
        product.setPrice(new BigDecimal("29.90"));
        product.setStockQuantity(8);
        product.setVersion(3L);

        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(productRepository.saveInventoryLog(any(InventoryLog.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product updatedProduct = productService.adjustStock(
                10L,
                3,
                true,
                3L,
                InventoryLockMode.OPTIMISTIC,
                "manual adjustment"
        );

        ArgumentCaptor<InventoryLog> logCaptor = ArgumentCaptor.forClass(InventoryLog.class);
        verify(productRepository).saveInventoryLog(logCaptor.capture());

        assertThat(updatedProduct.getStockQuantity()).isEqualTo(5);
        assertThat(logCaptor.getValue().getProductId()).isEqualTo(10L);
        assertThat(logCaptor.getValue().getChangeAmount()).isEqualTo(-3);
    }
}

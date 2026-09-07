package com.example.orderinventory.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.orderinventory.application.product.ProductApplicationService;
import com.example.orderinventory.application.product.dto.CreateProductRequest;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
class OrderFlowIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private ProductApplicationService productApplicationService;

    @Test
    void contextLoadsAndPersistsProduct() {
        int beforeSize = productApplicationService.listProducts().size();

        var savedProduct = productApplicationService.createProduct(
                new CreateProductRequest("Keyboard", "Mechanical keyboard", new BigDecimal("99.90"), 10)
        );

        assertThat(savedProduct.id()).isNotNull();
        assertThat(productApplicationService.listProducts()).hasSize(beforeSize + 1);
    }
}

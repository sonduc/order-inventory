package com.example.orderinventory.infrastructure.persistence.adapter;

import com.example.orderinventory.domain.product.InventoryLog;
import com.example.orderinventory.domain.product.Product;
import com.example.orderinventory.domain.product.ProductRepository;
import com.example.orderinventory.infrastructure.persistence.jpa.InventoryLogJpaRepository;
import com.example.orderinventory.infrastructure.persistence.jpa.ProductJpaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryAdapter implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;
    private final InventoryLogJpaRepository inventoryLogJpaRepository;

    public ProductRepositoryAdapter(
            ProductJpaRepository productJpaRepository,
            InventoryLogJpaRepository inventoryLogJpaRepository
    ) {
        this.productJpaRepository = productJpaRepository;
        this.inventoryLogJpaRepository = inventoryLogJpaRepository;
    }

    @Override
    public List<Product> findAll() {
        return productJpaRepository.findAll();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productJpaRepository.findById(id);
    }

    @Override
    public Optional<Product> findByIdForUpdate(Long id) {
        return productJpaRepository.findByIdForUpdate(id);
    }

    @Override
    public Product save(Product product) {
        return productJpaRepository.save(product);
    }

    @Override
    public InventoryLog saveInventoryLog(InventoryLog inventoryLog) {
        return inventoryLogJpaRepository.save(inventoryLog);
    }
}


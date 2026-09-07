package com.example.orderinventory.domain.product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    List<Product> findAll();

    Optional<Product> findById(Long id);

    Optional<Product> findByIdForUpdate(Long id);

    Product save(Product product);

    InventoryLog saveInventoryLog(InventoryLog inventoryLog);
}

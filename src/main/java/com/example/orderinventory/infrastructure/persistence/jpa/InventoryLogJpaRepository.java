package com.example.orderinventory.infrastructure.persistence.jpa;

import com.example.orderinventory.domain.product.InventoryLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryLogJpaRepository extends JpaRepository<InventoryLog, Long> {
}


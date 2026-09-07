package com.example.orderinventory.domain.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "inventory_logs")
public class InventoryLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Integer changeAmount;

    @Column(nullable = false)
    private Integer remainingStock;

    @Column(nullable = false, length = 255)
    private String reason;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected InventoryLog() {
    }

    public InventoryLog(Long productId, Integer changeAmount, Integer remainingStock, String reason) {
        this.productId = productId;
        this.changeAmount = changeAmount;
        this.remainingStock = remainingStock;
        this.reason = reason == null || reason.isBlank() ? "unspecified" : reason.trim();
    }

    @PrePersist
    void onCreate() {
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getChangeAmount() {
        return changeAmount;
    }

    public Integer getRemainingStock() {
        return remainingStock;
    }

    public String getReason() {
        return reason;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}


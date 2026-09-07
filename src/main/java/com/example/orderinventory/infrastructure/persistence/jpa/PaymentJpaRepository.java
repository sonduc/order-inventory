package com.example.orderinventory.infrastructure.persistence.jpa;

import com.example.orderinventory.domain.payment.Payment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrder_Id(Long orderId);
}


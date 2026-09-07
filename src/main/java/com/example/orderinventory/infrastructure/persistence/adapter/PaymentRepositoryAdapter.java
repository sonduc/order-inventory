package com.example.orderinventory.infrastructure.persistence.adapter;

import com.example.orderinventory.domain.payment.Payment;
import com.example.orderinventory.domain.payment.PaymentRepository;
import com.example.orderinventory.infrastructure.persistence.jpa.PaymentJpaRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentRepositoryAdapter implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;

    public PaymentRepositoryAdapter(PaymentJpaRepository paymentJpaRepository) {
        this.paymentJpaRepository = paymentJpaRepository;
    }

    @Override
    public Optional<Payment> findByOrderId(Long orderId) {
        return paymentJpaRepository.findByOrder_Id(orderId);
    }

    @Override
    public Payment save(Payment payment) {
        return paymentJpaRepository.save(payment);
    }
}


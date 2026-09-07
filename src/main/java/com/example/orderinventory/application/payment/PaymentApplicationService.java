package com.example.orderinventory.application.payment;

import com.example.orderinventory.application.payment.dto.PaymentRequest;
import com.example.orderinventory.application.payment.dto.PaymentResponse;
import com.example.orderinventory.domain.order.OrderService;
import com.example.orderinventory.domain.payment.Payment;
import com.example.orderinventory.domain.payment.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentApplicationService {

    private final OrderService orderService;
    private final PaymentService paymentService;

    public PaymentApplicationService(OrderService orderService, PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @Transactional
    public PaymentResponse processPayment(PaymentRequest request) {
        Payment payment = paymentService.processPayment(orderService.getRequiredOrder(request.orderId()));
        return new PaymentResponse(
                payment.getOrder().getId(),
                payment.getStatus(),
                payment.getAttemptedAt(),
                payment.getProviderReference()
        );
    }
}


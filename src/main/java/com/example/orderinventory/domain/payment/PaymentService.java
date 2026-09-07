package com.example.orderinventory.domain.payment;

import com.example.orderinventory.domain.order.Order;
import com.example.orderinventory.domain.order.OrderRepository;
import com.example.orderinventory.domain.order.OrderStatus;
import com.example.orderinventory.domain.product.InventoryLockMode;
import com.example.orderinventory.domain.product.ProductService;
import com.example.orderinventory.domain.payment.event.PaymentFailedEvent;
import com.example.orderinventory.domain.payment.event.PaymentSucceededEvent;
import com.example.orderinventory.domain.payment.exception.PaymentException;
import com.example.orderinventory.domain.shared.event.EventPublisher;
import java.time.Instant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final MockPaymentGateway mockPaymentGateway;
    private final EventPublisher eventPublisher;

    public PaymentService(
            PaymentRepository paymentRepository,
            OrderRepository orderRepository,
            ProductService productService,
            MockPaymentGateway mockPaymentGateway,
            EventPublisher eventPublisher
    ) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.mockPaymentGateway = mockPaymentGateway;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Payment processPayment(Order order) {
        if (order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.COMPLETED) {
            throw new PaymentException("Cannot process payment for order in status %s.".formatted(order.getStatus()));
        }

        MockPaymentGateway.PaymentGatewayResult gatewayResult = mockPaymentGateway.charge(order);

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAttemptedAt(Instant.now());
        payment.setProviderReference(gatewayResult.providerReference());

        if (gatewayResult.success()) {
            if (order.getStatus() == OrderStatus.PENDING) {
                order.confirm();
                orderRepository.save(order);
            }
            payment.setStatus(PaymentStatus.SUCCESS);
            Payment savedPayment = paymentRepository.save(payment);
            eventPublisher.publish(new PaymentSucceededEvent(order.getId(), savedPayment.getProviderReference()));
            return savedPayment;
        }

        if (order.getStatus() == OrderStatus.PENDING || order.getStatus() == OrderStatus.CONFIRMED) {
            order.cancel();
            orderRepository.save(order);
            order.getItems().forEach(item -> productService.adjustStock(
                    item.getProduct().getId(),
                    item.getQuantity(),
                    false,
                    null,
                    InventoryLockMode.PESSIMISTIC,
                    "Restored after payment failure for order %d".formatted(order.getId())
            ));
        }

        payment.setStatus(PaymentStatus.FAILED);
        Payment savedPayment = paymentRepository.save(payment);
        eventPublisher.publish(new PaymentFailedEvent(order.getId(), gatewayResult.failureReason()));
        return savedPayment;
    }
}

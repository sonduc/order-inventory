package com.example.orderinventory.presentation.rest;

import com.example.orderinventory.application.payment.PaymentApplicationService;
import com.example.orderinventory.application.payment.dto.PaymentRequest;
import com.example.orderinventory.application.payment.dto.PaymentResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentApplicationService paymentApplicationService;

    public PaymentController(PaymentApplicationService paymentApplicationService) {
        this.paymentApplicationService = paymentApplicationService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PAYMENT_PROCESS')")
    public PaymentResponse processPayment(@Valid @RequestBody PaymentRequest request) {
        return paymentApplicationService.processPayment(request);
    }
}

package com.example.orderinventory.presentation.rest;

import com.example.orderinventory.application.webhook.WebhookService;
import com.example.orderinventory.application.webhook.dto.WebhookDeliveryLog;
import com.example.orderinventory.application.webhook.dto.WebhookRegistrationRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/webhooks")
public class WebhookController {

    private final WebhookService webhookService;

    public WebhookController(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    @PostMapping("/test-delivery")
    @PreAuthorize("hasAuthority('WEBHOOK_MANAGE')")
    public WebhookDeliveryLog testDelivery(@Valid @RequestBody WebhookRegistrationRequest request) {
        return webhookService.dispatchTestWebhook(request);
    }
}

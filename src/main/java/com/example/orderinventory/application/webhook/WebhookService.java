package com.example.orderinventory.application.webhook;

import com.example.orderinventory.application.webhook.dispatcher.WebhookDispatcher;
import com.example.orderinventory.application.webhook.dto.WebhookDeliveryLog;
import com.example.orderinventory.application.webhook.dto.WebhookPayload;
import com.example.orderinventory.application.webhook.dto.WebhookRegistrationRequest;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class WebhookService {

    private final WebhookDispatcher webhookDispatcher;

    public WebhookService(WebhookDispatcher webhookDispatcher) {
        this.webhookDispatcher = webhookDispatcher;
    }

    public WebhookDeliveryLog dispatchTestWebhook(WebhookRegistrationRequest request) {
        String eventType = request.eventTypes() == null || request.eventTypes().isEmpty()
                ? "order.created"
                : request.eventTypes().get(0);
        WebhookPayload payload = new WebhookPayload(eventType, "test-aggregate", Map.of("source", "manual-test"));
        return webhookDispatcher.dispatch(request, payload);
    }
}


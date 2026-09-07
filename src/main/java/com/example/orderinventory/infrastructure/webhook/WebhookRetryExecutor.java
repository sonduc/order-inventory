package com.example.orderinventory.infrastructure.webhook;

import com.example.orderinventory.application.webhook.dispatcher.WebhookDispatcher;
import com.example.orderinventory.application.webhook.dispatcher.WebhookRetryPolicy;
import com.example.orderinventory.application.webhook.dto.WebhookDeliveryLog;
import com.example.orderinventory.application.webhook.dto.WebhookPayload;
import com.example.orderinventory.application.webhook.dto.WebhookRegistrationRequest;
import java.time.Instant;
import org.springframework.stereotype.Component;

@Component
public class WebhookRetryExecutor implements WebhookDispatcher {

    private final HttpWebhookClient httpWebhookClient;
    private final WebhookSignatureValidator webhookSignatureValidator;

    public WebhookRetryExecutor(
            HttpWebhookClient httpWebhookClient,
            WebhookSignatureValidator webhookSignatureValidator
    ) {
        this.httpWebhookClient = httpWebhookClient;
        this.webhookSignatureValidator = webhookSignatureValidator;
    }

    @Override
    public WebhookDeliveryLog dispatch(WebhookRegistrationRequest registrationRequest, WebhookPayload payload) {
        WebhookRetryPolicy retryPolicy = new WebhookRetryPolicy(3, 250L);
        String secret = registrationRequest.secret() == null ? "" : registrationRequest.secret();
        String signature = webhookSignatureValidator.sign(secret, payload.toString());

        boolean successful = false;
        int attempts = 0;
        for (int attempt = 1; attempt <= retryPolicy.maxAttempts(); attempt++) {
            attempts = attempt;
            successful = httpWebhookClient.post(registrationRequest.targetUrl(), payload, signature);
            if (successful) {
                break;
            }
        }

        return new WebhookDeliveryLog(
                registrationRequest.targetUrl(),
                payload.eventType(),
                successful,
                attempts,
                Instant.now()
        );
    }
}


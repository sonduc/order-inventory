package com.example.orderinventory.infrastructure.webhook;

import com.example.orderinventory.application.webhook.dto.WebhookPayload;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class HttpWebhookClient {

    private final RestClient webhookRestClient;

    public HttpWebhookClient(RestClient webhookRestClient) {
        this.webhookRestClient = webhookRestClient;
    }

    public boolean post(String targetUrl, WebhookPayload payload, String signature) {
        try {
            ResponseEntity<Void> response = webhookRestClient.post()
                    .uri(targetUrl)
                    .header("X-Webhook-Signature", signature)
                    .body(payload)
                    .retrieve()
                    .toBodilessEntity();
            return response.getStatusCode().is2xxSuccessful();
        } catch (RestClientException exception) {
            return false;
        }
    }
}

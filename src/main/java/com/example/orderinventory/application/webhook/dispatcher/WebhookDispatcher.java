package com.example.orderinventory.application.webhook.dispatcher;

import com.example.orderinventory.application.webhook.dto.WebhookDeliveryLog;
import com.example.orderinventory.application.webhook.dto.WebhookPayload;
import com.example.orderinventory.application.webhook.dto.WebhookRegistrationRequest;

public interface WebhookDispatcher {

    WebhookDeliveryLog dispatch(WebhookRegistrationRequest registrationRequest, WebhookPayload payload);
}


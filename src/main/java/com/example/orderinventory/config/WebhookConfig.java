package com.example.orderinventory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class WebhookConfig {

    @Bean
    RestClient webhookRestClient() {
        return RestClient.builder().build();
    }
}


package com.renan.desafio.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.renan.desafio.dto.WebhookEvent;
import com.renan.desafio.exceptions.InvalidSignatureException;
import com.renan.desafio.kafka.producer.WebhookEventRequestProducer;
import com.renan.desafio.util.SignatureValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class WebhookService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SignatureValidator signatureValidator;

    @Autowired
    private WebhookEventRequestProducer webhookEventRequestProducer;

    public void processWebhookEvent(List<WebhookEvent> payload, String signature) throws JsonProcessingException {
        log.info("Starting webhook processing");

        validatePayload(payload);
        validateSignature(payload, signature);

        String payloadJson = serializePayload(payload);
        sendToKafka(payloadJson);
    }

    private String serializePayload(List<WebhookEvent> payload) throws JsonProcessingException {
        try {
            String payloadJson = objectMapper.writeValueAsString(payload);
            log.info("Payload: {}", payloadJson);

            return payloadJson;
        } catch (JsonProcessingException e) {
            log.error("Error serializing webhook payload: {}", e.getMessage());

            throw new JsonProcessingException("Error serializing webhook payload", e) {};
        }
    }

    private void sendToKafka(String payloadJson) {
        try {
            webhookEventRequestProducer.sendMessage(payloadJson);

            log.info("Webhook event sent to Kafka topic");
        } catch (Exception e) {
            log.error("Error sending webhook event to Kafka topic: {}", e.getMessage());

            throw new RuntimeException("Error sending webhook event to Kafka topic", e);
        }
    }

    private void validatePayload(List<WebhookEvent> payload) {
        if (payload == null || payload.isEmpty()) {
            log.error("Payload is null or empty");

            throw new IllegalArgumentException("Payload cannot be null or empty");
        }
    }

    private void validateSignature(List<WebhookEvent> payload, String signature) throws JsonProcessingException {
        if (signature == null || signature.isEmpty()) {
            log.error("Signature is null or empty");

            throw new IllegalArgumentException("Signature cannot be null or empty");
        }

        String payloadJson = objectMapper.writeValueAsString(payload);

        if (!signatureValidator.isValid(payloadJson, signature)) {
            log.error("Invalid signature");

            throw new InvalidSignatureException("Invalid signature");
        }
    }
}

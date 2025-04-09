package com.renan.desafio.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.renan.desafio.dto.WebhookEvent;
import com.renan.desafio.exceptions.InvalidSignatureException;
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

    public void processWebhookEvent(List<WebhookEvent> payload, String signature) throws JsonProcessingException {
        log.info("Starting webhook processing");

        validatePayload(payload);
        validateSignature(payload, signature);

        //TODO -> Como não foi especificado o que seria "processar o webhook", fica a critério do desenvolvedor
        // Aqui você pode adicionar a lógica para processar o webhook, como salvar os dados no banco de dados ou enviar uma notificação.
        // Ou enviar o evento para uma fila rabbitMQ ou um tópico Kafka.

        log.info("Webhook processed successfully");
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

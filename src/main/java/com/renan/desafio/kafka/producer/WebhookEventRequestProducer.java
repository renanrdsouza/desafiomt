package com.renan.desafio.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.renan.desafio.dto.WebhookEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebhookEventRequestProducer {
    @Value("${topicos.contact-creation.request.topic}")
    private String contactCreationRequestTopic;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public String sendMessage(String webhookEvent) throws JsonProcessingException {
        String conteudo = objectMapper.writeValueAsString(webhookEvent);
        kafkaTemplate.send(contactCreationRequestTopic, conteudo);
        return "Contact creation event sent for processing";
    }
}

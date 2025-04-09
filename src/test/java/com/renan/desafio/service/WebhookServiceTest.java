package com.renan.desafio.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.renan.desafio.dto.WebhookEvent;
import com.renan.desafio.exceptions.InvalidSignatureException;
import com.renan.desafio.kafka.producer.WebhookEventRequestProducer;
import com.renan.desafio.util.SignatureValidator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class WebhookServiceTest {
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private SignatureValidator signatureValidator;

    @Mock
    private WebhookEventRequestProducer webhookEventRequestProducer;

    @InjectMocks
    private WebhookService webhookService;

@Test
void processWebhookEvent_shouldProcessSuccessfullyAndSendMessageToKafka() throws Exception {
    var signature = "validSignature";
    var mockedPayload = List.of(new WebhookEvent(
            1,
            123,
            123,
            123,
            123,
            "test",
            1,
            123,
            "test",
            "test",
            "123"
    ));

    when(objectMapper.writeValueAsString(mockedPayload)).thenReturn("testPayloadJson");
    when(signatureValidator.isValid("testPayloadJson", signature)).thenReturn(true);

    webhookService.processWebhookEvent(mockedPayload, signature);

    verify(objectMapper, times(2)).writeValueAsString(mockedPayload);
    verify(signatureValidator, times(1)).isValid("testPayloadJson", signature);
    verify(webhookEventRequestProducer, times(1)).sendMessage("testPayloadJson");
}

@Test
void processWebhookEvent_shouldThrowException_whenSignatureIsInvalid() throws Exception {
    var signature = "invalidSignature";
    var mockedPayload = List.of(new WebhookEvent(
            1,
            123,
            123,
            123,
            123,
            "test",
            1,
            123,
            "test",
            "test",
            "123"
    ));

    var payloadJson = "testPayloadJson";
    when(objectMapper.writeValueAsString(mockedPayload)).thenReturn(payloadJson);
    when(signatureValidator.isValid(payloadJson, signature)).thenReturn(false);

    assertThrows(InvalidSignatureException.class, () -> {
        webhookService.processWebhookEvent(mockedPayload, signature);
    });

    verify(webhookEventRequestProducer, times(0)).sendMessage(payloadJson);
}

    @Test
    void processWebhookEvent_shouldThrownInvalidSignatureException() throws JsonProcessingException {
        var signature = "fakeSignature";
        var mockedPayload = List.of(new WebhookEvent(
                1,
                123,
                123,
                123,
                123,
                "test",
                1,
                123,
                "test",
                "test",
                "123"
        ));

        when(objectMapper.writeValueAsString(anyList())).thenReturn("testPayloadJson");

        assertThrows(InvalidSignatureException.class, () -> {
            webhookService.processWebhookEvent(mockedPayload, signature);
        });
    }

    @Test
    void processWebhookEvent_shouldThrowException_whenPayloadIsEmpty() {
        var signature = "fakeSignature";

        assertThrows(IllegalArgumentException.class, () -> {
            webhookService.processWebhookEvent(List.of(), signature);
        });
    }

@Test
void processWebhookEvent_shouldThrowException_whenJsonProcessingFails() throws JsonProcessingException {
    var signature = "validSignature";
    var mockedPayload = List.of(new WebhookEvent(
            1,
            123,
            123,
            123,
            123,
            "test",
            1,
            123,
            "test",
            "test",
            "123"
    ));

    var payloadJson = "testPayloadJson";

    when(objectMapper.writeValueAsString(anyList())).thenThrow(JsonProcessingException.class);

    assertThrows(JsonProcessingException.class, () -> {
        webhookService.processWebhookEvent(mockedPayload, signature);
    });

    verify(webhookEventRequestProducer, times(0)).sendMessage(payloadJson);
}

    @Test
    void processWebhookEvent_shouldThrowException_whenEncodingFails() throws Exception {
        var signature = "validSignature";
        var mockedPayload = List.of(new WebhookEvent(
                1,
                123,
                123,
                123,
                123,
                "test",
                1,
                123,
                "test",
                "test",
                "123"
        ));

        when(objectMapper.writeValueAsString(anyList())).thenThrow(JsonProcessingException.class);

        assertThrows(JsonProcessingException.class, () -> {
            webhookService.processWebhookEvent(mockedPayload, signature);
        });
    }
}
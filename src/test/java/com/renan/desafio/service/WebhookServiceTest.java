package com.renan.desafio.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.renan.desafio.dto.WebhookEvent;
import com.renan.desafio.exceptions.InvalidSignatureException;
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

    @InjectMocks
    private WebhookService webhookService;

    @Test
    void processWebhookEvent_shouldProcessSuccessfully() throws Exception {
        var signature = "d092bcf92ae62a2b1dc8a7312d82704e77b7e2272966f3f128478ca9a0951fd1";
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

        verify(objectMapper, times(1)).writeValueAsString(mockedPayload);
        verify(signatureValidator, times(1)).isValid("testPayloadJson", signature);
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

        when(objectMapper.writeValueAsString(anyList())).thenThrow(JsonProcessingException.class);

        assertThrows(JsonProcessingException.class, () -> {
            webhookService.processWebhookEvent(mockedPayload, signature);
        });
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
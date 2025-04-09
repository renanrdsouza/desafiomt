package com.renan.desafio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renan.desafio.configuration.SecurityConfiguration;
import com.renan.desafio.dto.WebhookEvent;
import com.renan.desafio.service.WebhookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WebhookController.class)
@Import(SecurityConfiguration.class)
class WebhookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WebhookService webhookService;

    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(value = "spring")
    @Test
    void handleContactCreationWebhook_shouldReturnOk() throws Exception {
        doNothing().when(webhookService).processWebhookEvent(List.of(), "mock-signature");

        List<WebhookEvent> payload = List.of(new WebhookEvent());
        String payloadJson = objectMapper.writeValueAsString(payload);

        mockMvc.perform(post("/webhook/contact-creation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-HubSpot-Signature", "mock-signature")
                        .content(payloadJson))
                .andExpect(status().isOk());

        verify(webhookService).processWebhookEvent(any(List.class), eq("mock-signature"));
    }
}
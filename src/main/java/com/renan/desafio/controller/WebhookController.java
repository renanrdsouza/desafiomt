package com.renan.desafio.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.renan.desafio.dto.WebhookEvent;
import com.renan.desafio.service.WebhookService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/webhook")
@Slf4j
public class WebhookController {

    @Autowired
    private WebhookService service;

    @Operation(hidden = true)
    @PostMapping("/contact-creation")
    public ResponseEntity<Void> handleContactCreationWebhook(
            @RequestBody List<WebhookEvent> payload,
            @RequestHeader HttpHeaders headers
    ) throws JsonProcessingException {
        log.info("Processing contact creation webhook");

        service.processWebhookEvent(payload, headers.getFirst("X-HubSpot-Signature"));

        return ResponseEntity.ok().build();
    }
}

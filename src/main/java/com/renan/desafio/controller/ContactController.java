package com.renan.desafio.controller;

import com.renan.desafio.dto.ContactData;
import com.renan.desafio.service.ContactService;
import com.renan.desafio.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/contacts")
@Slf4j
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private TokenService tokenService;

    @Operation(description = "Create a new contact")
    @PostMapping
    public ResponseEntity<String> createContact(@RequestBody ContactData contactData) {
        log.info("Processing contact creation");

        String accessToken = tokenService.getAccessToken();

        var contactCreated = contactService.createContact(contactData, accessToken);

        return ResponseEntity.created((URI) contactCreated.get("Location")).build();
    }
}

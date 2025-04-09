package com.renan.desafio.controller;

import com.renan.desafio.dto.AuthorizationUrl;
import com.renan.desafio.service.AuthorizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/oauth")
public class OAuthController {

    @Autowired
    private AuthorizationService service;

    @GetMapping("/authorize")
    public ResponseEntity<AuthorizationUrl> generateAuthorizationUrl() {
        log.info("Processing authorization URL");

        return ResponseEntity.ok(service.buildAuthorizationUrl());
    }
}

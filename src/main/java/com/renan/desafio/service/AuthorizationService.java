package com.renan.desafio.service;

import com.renan.desafio.dto.AuthorizationUrl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthorizationService {

    @Value("${hubspot.client.id}")
    private String clientId;

    @Value("${hubspot.redirect.uri}")
    private String redirectUri;

    @Value("${hubspot.scope}")
    private String scope;

    @Value("${hubspot.authorization.url}")
    private String authorizationUrl;

    @Value("${hubspot.token.url}")
    private String tokenUrl;

    @Value("${hubspot.client.secret}")
    private String clientSecret;

    public AuthorizationUrl buildAuthorizationUrl() {
        log.info("Building authorization URL");

        var newUrl = new AuthorizationUrl();
        newUrl.setUrl(
                String.format(
                        "%s?client_id=%s&redirect_uri=%s&scope=%s&response_type=code",
                        authorizationUrl,
                        clientId,
                        redirectUri,
                        scope
                )
        );

        log.info("Authorization URL built successfully");
        return newUrl;
    }
}
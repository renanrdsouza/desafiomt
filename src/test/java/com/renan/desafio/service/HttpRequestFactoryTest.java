package com.renan.desafio.service;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.util.MultiValueMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HttpRequestFactoryTest {
    private final HttpRequestFactory httpRequestFactory = new HttpRequestFactory();

    @Test
    void createTokenRequestEntity_shouldReturnValidHttpEntity_whenAllParametersAreProvided() {
        HttpEntity<MultiValueMap<String, String>> requestEntity = httpRequestFactory.createTokenRequestEntity(
                "clientId", "clientSecret", "redirectUri", "authorizationCode");

        assertNotNull(requestEntity);
        assertNotNull(requestEntity.getBody());
        assertEquals("authorization_code", requestEntity.getBody().getFirst("grant_type"));
        assertEquals("clientId", requestEntity.getBody().getFirst("client_id"));
        assertEquals("clientSecret", requestEntity.getBody().getFirst("client_secret"));
        assertEquals("redirectUri", requestEntity.getBody().getFirst("redirect_uri"));
        assertEquals("authorizationCode", requestEntity.getBody().getFirst("code"));
        assertEquals("application/x-www-form-urlencoded", requestEntity.getHeaders().getFirst("Content-Type"));
    }
}
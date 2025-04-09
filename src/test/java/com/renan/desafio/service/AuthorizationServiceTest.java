package com.renan.desafio.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.spy;

@SpringBootTest
class AuthorizationServiceTest {

    @InjectMocks
    private AuthorizationService authorizationService = spy(new AuthorizationService());

    public AuthorizationServiceTest() {

    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void buildAuthorizationUrl_shouldReturnCorrectUrl() {
        var authorizationUrl = authorizationService.buildAuthorizationUrl();
        assertNotNull(authorizationUrl);
        assertTrue(authorizationUrl.getUrl().contains("client_id="));
        assertTrue(authorizationUrl.getUrl().contains("redirect_uri="));
        assertTrue(authorizationUrl.getUrl().contains("scope="));
        assertTrue(authorizationUrl.getUrl().contains("response_type=code"));
    }
}
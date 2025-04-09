package com.renan.desafio.service;

import com.renan.desafio.exceptions.TokenAcquisitionException;
import com.renan.desafio.model.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class AuthorizationServiceTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private HttpRequestFactory httpRequestFactory;

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

    @Test
    void processOauthCallback_shouldReturnAccessToken_whenRequestTokenIsMocked() {
        var tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken("access_token");
        tokenResponse.setRefreshToken("refresh_token");

        ResponseEntity<TokenResponse> mockResponse = ResponseEntity.ok(tokenResponse);
        ReflectionTestUtils.setField(authorizationService, "tokenUrl", "testTokenUrl");

        doReturn(mockResponse).when(authorizationService).requestToken(anyString());

        var accessToken = authorizationService.processOauthCallback("authorization_code");

        assertEquals("access_token", accessToken);
        verify(tokenService).storeTokens("access_token", "refresh_token");
    }

    @Test
    void processOauthCallback_shouldThrowException_whenResponseIsUnsuccessful() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(TokenResponse.class)))
                .thenReturn(ResponseEntity.status(400).build());
        ReflectionTestUtils.setField(authorizationService, "tokenUrl", "testTokenUrl");

        assertThrows(TokenAcquisitionException.class, () -> authorizationService.processOauthCallback("authorization_code"));
    }

    @Test
    void processOauthCallback_shouldThrowException_whenResponseBodyIsNull() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(TokenResponse.class)))
                .thenReturn(ResponseEntity.ok(null));
        ReflectionTestUtils.setField(authorizationService, "tokenUrl", "testTokenUrl");

        assertThrows(TokenAcquisitionException.class, () -> authorizationService.processOauthCallback("authorization_code"));
    }
}
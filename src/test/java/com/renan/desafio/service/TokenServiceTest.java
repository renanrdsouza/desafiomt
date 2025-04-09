package com.renan.desafio.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.renan.desafio.exceptions.RefreshTokenNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Cache<String, String> tokenCache;

    @InjectMocks
    private TokenService tokenService;

    @Test
    void getAccessToken_shouldReturnTokenFromCache_whenTokenIsPresent() {
        when(tokenCache.getIfPresent(TokenService.ACCESS_TOKEN)).thenReturn("cached_access_token");

        String accessToken = tokenService.getAccessToken();

        assertEquals("cached_access_token", accessToken);
    }

    @Test
    void getAccessToken_shouldRefreshToken_whenTokenIsNotPresentInCache() {
        when(tokenCache.getIfPresent(TokenService.ACCESS_TOKEN)).thenReturn(null);
        when(tokenCache.getIfPresent(TokenService.REFRESH_TOKEN)).thenReturn("refresh_token");
        Map<String, Object> response = new HashMap<>();
        response.put(TokenService.ACCESS_TOKEN, "new_access_token");
        response.put(TokenService.EXPIRES_IN, 3600);
        when(restTemplate.postForObject(anyString(), any(), eq(Map.class))).thenReturn(response);
        ReflectionTestUtils.setField(tokenService, "tokenUrl", "test-token-url");

        String accessToken = tokenService.getAccessToken();

        assertEquals("new_access_token", accessToken);
        verify(tokenCache).put(TokenService.ACCESS_TOKEN, "new_access_token");
        verify(tokenCache).put(TokenService.EXPIRES_IN, "3600");
    }

    @Test
    void getAccessToken_shouldThrowException_whenRefreshTokenIsNotPresent() {
        when(tokenCache.getIfPresent(TokenService.ACCESS_TOKEN)).thenReturn(null);
        when(tokenCache.getIfPresent(TokenService.REFRESH_TOKEN)).thenReturn(null);

        assertThrows(RefreshTokenNotFoundException.class, () -> tokenService.getAccessToken());
    }

    @Test
    void storeTokens_shouldStoreTokensInCache() {
        tokenService.storeTokens("access_token", "refresh_token");

        verify(tokenCache).put(TokenService.ACCESS_TOKEN, "access_token");
        verify(tokenCache).put(TokenService.REFRESH_TOKEN, "refresh_token");
    }

    @Test
    void refreshAccessToken_shouldThrowException_whenResponseIsNull() {
        assertThrows(RefreshTokenNotFoundException.class, () -> tokenService.getAccessToken());
    }

    @Test
    void refreshAccessToken_shouldThrowException_whenResponseDoesNotContainAccessToken() {
        when(tokenCache.getIfPresent(TokenService.ACCESS_TOKEN)).thenReturn(null);
        when(tokenCache.getIfPresent(TokenService.REFRESH_TOKEN)).thenReturn("refresh_token");
        ReflectionTestUtils.setField(tokenService, "tokenUrl", "test-token-url");
        Map<String, Object> response = new HashMap<>();
        when(restTemplate.postForObject(anyString(), any(), eq(Map.class))).thenReturn(response);

        assertThrows(IllegalStateException.class, () -> tokenService.getAccessToken());
    }
}
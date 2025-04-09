package com.renan.desafio.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.renan.desafio.exceptions.RefreshTokenNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class TokenService {

    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String GRANT_TYPE = "grant_type";
    public static final String CLIENT_ID = "client_id";
    public static final String CLIENT_SECRET = "client_secret";
    public static final String EXPIRES_IN = "expires_in";

    @Value("${hubspot.client.id}")
    private String clientId;

    @Value("${hubspot.client.secret}")
    private String clientSecret;

    @Value("${hubspot.token.url}")
    private String tokenUrl;

    @Autowired
    private Cache<String, String> tokenCache;

    @Autowired
    private RestTemplate restTemplate;

    public String getAccessToken() {
        log.info("Obtaining access token from cache");

        String accessToken = tokenCache.getIfPresent(ACCESS_TOKEN);

        if (accessToken == null) {
            accessToken = refreshAccessToken();
        }

        log.info("Access token obtained successfully");

        return accessToken;
    }

    public void storeTokens(String accessToken, String refreshToken) {
        tokenCache.put(ACCESS_TOKEN, accessToken);
        tokenCache.put(REFRESH_TOKEN, refreshToken);
    }

    private String refreshAccessToken() {
        log.info("Refreshing access token");

        String refreshToken = getRefreshTokenFromCache();

        Map<String, String> requestBody = buildRefreshTokenRequestBody(refreshToken);

        Map<String, Object> response = sendRefreshTokenRequest(requestBody);

        return processRefreshTokenResponse(response);
    }

    private String getRefreshTokenFromCache() {
        String refreshToken = tokenCache.getIfPresent(REFRESH_TOKEN);
        if (refreshToken == null) {
            log.error("Refresh token not found");
            throw new RefreshTokenNotFoundException("Refresh token not found. It is necessary to authenticate again.");
        }

        return refreshToken;
    }

    private Map<String, String> buildRefreshTokenRequestBody(String refreshToken) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put(GRANT_TYPE, REFRESH_TOKEN);
        requestBody.put(CLIENT_ID, clientId);
        requestBody.put(CLIENT_SECRET, clientSecret);
        requestBody.put(REFRESH_TOKEN, refreshToken);

        return requestBody;
    }

    private Map<String, Object> sendRefreshTokenRequest(Map<String, String> requestBody) {
        return restTemplate.postForObject(tokenUrl, requestBody, Map.class);
    }

    private String processRefreshTokenResponse(Map<String, Object> response) {
        if (response != null && response.containsKey(ACCESS_TOKEN)) {
            String newAccessToken = (String) response.get(ACCESS_TOKEN);
            Integer expiresIn = (Integer) response.get(EXPIRES_IN);

            tokenCache.put(ACCESS_TOKEN, newAccessToken);
            tokenCache.put(EXPIRES_IN, expiresIn.toString());

            log.info("Access token refreshed successfully");

            return newAccessToken;
        }

        log.error("Failed to refresh access token");
        throw new IllegalStateException("Erro ao renovar o token de acesso.");
    }
}

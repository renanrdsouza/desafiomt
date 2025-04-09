package com.renan.desafio.service;

import com.renan.desafio.exceptions.TokenAcquisitionException;
import com.renan.desafio.dto.AuthorizationUrl;
import com.renan.desafio.model.TokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

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

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HttpRequestFactory httpRequestFactory;

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

    public String processOauthCallback(String authorizationCode) {
        try {
            ResponseEntity<TokenResponse> response = requestToken(authorizationCode);
            validateTokenResponse(response);

            TokenResponse tokenResponse = response.getBody();
            storeTokens(tokenResponse);

            log.info("Access token acquired successfully");
            return tokenResponse.getAccessToken();
        } catch (RestClientException ex) {
            log.error("Error during token acquisition: {}", ex.getMessage());
            throw new TokenAcquisitionException("Failed to acquire token due to a network error", ex);
        }
    }

    protected ResponseEntity<TokenResponse> requestToken(String authorizationCode) {
        HttpEntity<?> requestEntity = httpRequestFactory.createTokenRequestEntity(
                clientId, clientSecret, redirectUri, authorizationCode
        );
        return restTemplate.exchange(tokenUrl, HttpMethod.POST, requestEntity, TokenResponse.class);
    }

    private void validateTokenResponse(ResponseEntity<TokenResponse> response) {
        if (response == null || response.getBody() == null || !response.getStatusCode().is2xxSuccessful()) {
            throw new TokenAcquisitionException("Failed to acquire token: invalid response");
        }
    }

    private void storeTokens(TokenResponse tokenResponse) {
        tokenService.storeTokens(tokenResponse.getAccessToken(), tokenResponse.getRefreshToken());
    }
}
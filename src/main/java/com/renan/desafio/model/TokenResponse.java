package com.renan.desafio.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenResponse {
    @JsonProperty("token_type")
    String tokenType;

    @JsonProperty("refresh_token")
    String refreshToken;

    @JsonProperty("expires_in")
    int expiresIn;

    @JsonProperty("access_token")
    String accessToken;
}

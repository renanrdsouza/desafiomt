package com.renan.desafio.controller;

import com.renan.desafio.configuration.SecurityConfiguration;
import com.renan.desafio.dto.AuthorizationUrl;
import com.renan.desafio.service.AuthorizationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OAuthController.class)
@Import(SecurityConfiguration.class)
class OAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthorizationService authorizationService;

    @WithMockUser(value = "spring")
    @Test
    void generateAuthorizationUrl_shouldReturnAuthorizationUrl() throws Exception {
        var authorizationUrl = new AuthorizationUrl();
        authorizationUrl.setUrl("https://example.com/auth");

        when(authorizationService.buildAuthorizationUrl()).thenReturn(authorizationUrl);

        mockMvc.perform(get("/oauth/authorize"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.url").value("https://example.com/auth"));
    }

    @Test
    void processOAuthCallback_shouldReturnAccessToken() throws Exception {
        var authorizationCode = "testCode";
        var accessToken = "testAccessToken";
        when(authorizationService.processOauthCallback(authorizationCode)).thenReturn(accessToken);

        mockMvc.perform(get("/oauth/callback").param("code", authorizationCode))
                .andExpect(status().isOk())
                .andExpect(content().string(accessToken));
    }
}
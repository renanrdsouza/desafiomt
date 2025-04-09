package com.renan.desafio.controller;

import com.renan.desafio.configuration.SecurityConfiguration;
import com.renan.desafio.dto.ContactData;
import com.renan.desafio.service.ContactService;
import com.renan.desafio.service.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContactController.class)
@Import(SecurityConfiguration.class)
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ContactService contactService;

    @MockitoBean
    private TokenService tokenService;

    @WithMockUser(value = "spring")
    @Test
    void createContact_shouldReturnCreatedStatus() throws Exception {
        when(tokenService.getAccessToken()).thenReturn("mockedAccessToken");

        when(contactService.createContact(any(ContactData.class), eq("mockedAccessToken")))
                .thenReturn(Map.of("Location", URI.create("http://localhost:8080/contacts/1")));

        String contactDataJson = """
                {
                    "name": "John Doe",
                    "email": "johndoe@example.com",
                    "phone": "123456789"
                }
                """;

        mockMvc.perform(post("/contacts/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contactDataJson))
                .andExpect(status().isCreated());
    }
}
package com.renan.desafio.service;

import com.renan.desafio.dto.ContactData;
import com.renan.desafio.exceptions.ContactCreationFailedException;
import com.renan.desafio.model.HubSpotContactResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ContactService contactService;

    @Test
    void createContact_shouldReturnContactDetails_whenResponseIsSuccessful() {
        ContactData contactData = new ContactData();
        HubSpotContactResponse hubSpotContactResponse = new HubSpotContactResponse();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("http://example.com/contact/1"));

        when(restTemplate.exchange(eq(ContactService.HUBSPOT_URL), eq(HttpMethod.POST), any(HttpEntity.class), eq(HubSpotContactResponse.class)))
                .thenReturn(new ResponseEntity<>(hubSpotContactResponse, headers, HttpStatus.CREATED));

        Map<String, Object> result = contactService.createContact(contactData, "access_token");

        assertNotNull(result);
        assertEquals(URI.create("http://example.com/contact/1"), result.get("Location"));
        assertEquals(hubSpotContactResponse, result.get("Contact"));
    }

    @Test
    void createContact_shouldThrowException_whenResponseIsUnsuccessful() {
        ContactData contactData = new ContactData();

        when(restTemplate.exchange(eq(ContactService.HUBSPOT_URL), eq(HttpMethod.POST), any(HttpEntity.class), eq(HubSpotContactResponse.class)))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));

        assertThrows(ContactCreationFailedException.class, () -> contactService.createContact(contactData, "access_token"));
    }

    @Test
    void createContact_shouldThrowException_whenResponseBodyIsNull() {
        ContactData contactData = new ContactData();

        when(restTemplate.exchange(eq(ContactService.HUBSPOT_URL), eq(HttpMethod.POST), any(HttpEntity.class), eq(HubSpotContactResponse.class)))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        assertThrows(ContactCreationFailedException.class, () -> contactService.createContact(contactData, "access_token"));
    }
}
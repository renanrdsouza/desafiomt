package com.renan.desafio.service;

import com.renan.desafio.exceptions.ContactCreationFailedException;
import com.renan.desafio.model.HubSpotContactResponse;
import com.renan.desafio.dto.ContactData;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ContactService {

    public static final String HUBSPOT_URL = "https://api.hubapi.com/crm/v3/objects/contacts";
    public static final String CREATE_CONTACT_LIMITER = "createContactLimiter";

    @Autowired
    private RestTemplate restTemplate;

    @RateLimiter(name = CREATE_CONTACT_LIMITER)
    public Map<String, Object> createContact(ContactData contactData, String accessToken) {
        log.info("Creating contact in HubSpot");

        HttpHeaders headers = createHeaders(accessToken);
        HttpEntity<ContactData> request = new HttpEntity<>(contactData, headers);

        ResponseEntity<HubSpotContactResponse> response = restTemplate.exchange(
                HUBSPOT_URL,
                HttpMethod.POST,
                request,
                HubSpotContactResponse.class
        );

        return handleResponse(response);
    }

    private HttpHeaders createHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private Map<String, Object> handleResponse(ResponseEntity<HubSpotContactResponse> response) {
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ContactCreationFailedException("Failed to create contact");
        }

        Map<String, Object> contactCreated = new HashMap<>();
        contactCreated.put("Location", response.getHeaders().getLocation());
        contactCreated.put("Contact", response.getBody());

        log.info("Contact created successfully");
        return contactCreated;
    }
}

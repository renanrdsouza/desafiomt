package com.renan.desafio.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateHttpClient implements HttpClient {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public <T> ResponseEntity<T> post(String url, HttpEntity<?> requestEntity, Class<T> responseType) {
        return restTemplate.postForEntity(url, requestEntity, responseType);
    }
}

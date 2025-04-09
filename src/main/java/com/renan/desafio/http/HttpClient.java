package com.renan.desafio.http;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

public interface HttpClient {
    <T> ResponseEntity<T> post(String url, HttpEntity<?> requestEntity, Class<T> responseType);
}
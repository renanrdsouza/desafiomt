package com.renan.desafio.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/*
* Esta classe implementa a interface HttpClient e utiliza o RestTemplate para fazer requisições HTTP.
* Ela seria uma melhoria futura para o projeto porque encapsula a lógica de requisições HTTP,
* permitindo que o código seja mais limpo e fácil de manter.
*/
@Component
public class RestTemplateHttpClient implements HttpClient {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public <T> ResponseEntity<T> post(String url, HttpEntity<?> requestEntity, Class<T> responseType) {
        return restTemplate.postForEntity(url, requestEntity, responseType);
    }
}

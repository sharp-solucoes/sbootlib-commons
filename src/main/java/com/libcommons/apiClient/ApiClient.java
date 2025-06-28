package com.libcommons.apiClient;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Getter
@Setter
@RequiredArgsConstructor
public class ApiClient<T, R> {

    private final RestTemplate restTemplate;

    @Value("${api.base.url}")
    private String baseUrl;

    @Value("${api.key}")
    private String apiKey;

    private HttpHeaders createHeaders(MediaType contentType) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", apiKey);
        if (contentType != null) {
            headers.setContentType(contentType);
        }
        return headers;
    }

    public R get(String path, Class<R> responseType) {
        String url = baseUrl + path;
        HttpEntity<Void> entity = new HttpEntity<>(createHeaders(null));
        ResponseEntity<R> response = restTemplate.exchange(url, HttpMethod.GET, entity, responseType);
        return response.getBody();
    }

    public R post(String path, T payload, Class<R> responseType) {
        String url = baseUrl + path;
        HttpEntity<T> entity = new HttpEntity<>(payload, createHeaders(MediaType.APPLICATION_JSON));
        ResponseEntity<R> response = restTemplate.exchange(url, HttpMethod.POST, entity, responseType);
        return response.getBody();
    }

    public R put(String path, T payload, Class<R> responseType) {
        String url = baseUrl + path;
        HttpEntity<T> entity = new HttpEntity<>(payload, createHeaders(MediaType.APPLICATION_JSON));
        ResponseEntity<R> response = restTemplate.exchange(url, HttpMethod.PUT, entity, responseType);
        return response.getBody();
    }

    public void delete(String path) {
        String url = baseUrl + path;
        HttpEntity<Void> entity = new HttpEntity<>(createHeaders(null));
        restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
    }

}

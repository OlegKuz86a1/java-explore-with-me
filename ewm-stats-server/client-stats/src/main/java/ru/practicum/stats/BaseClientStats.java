package ru.practicum.stats;

import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public class BaseClientStats {
    private final RestTemplate restTemplate;

    public BaseClientStats(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    protected <T> void post(String path, T body) {
        this.responseMaker(HttpMethod.POST, path, null, body);
    }

    protected ResponseEntity<Object> get(String path, Map<String, Object> parameter) {
        return this.responseMaker(HttpMethod.GET, path, parameter, null);
    }

    private <T> ResponseEntity<Object> responseMaker(HttpMethod method, String path,
                                                     @Nullable Map<String, Object> parameters, @Nullable T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, this.getHeaders());
        ResponseEntity<Object> response;
        try {
            if (parameters != null) {
                response = this.restTemplate.exchange(path, method, requestEntity, Object.class, parameters);
            } else {
                response = this.restTemplate.exchange(path, method, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareResponse(response);
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    private static ResponseEntity<Object> prepareResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        } else {
            ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());
            return response.hasBody() ? responseBuilder.body(response.getBody()) : responseBuilder.build();
        }
    }
}
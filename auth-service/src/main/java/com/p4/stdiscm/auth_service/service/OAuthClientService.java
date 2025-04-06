package com.p4.stdiscm.auth_service.service;

import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;

import java.util.Map;

@Service
public class OAuthClientService {

    private final RestTemplate restTemplate;
    private final CircuitBreaker oauthProviderCircuitBreaker;

    public OAuthClientService(
            CircuitBreakerFactory<?, ?> circuitBreakerFactory) {
        this.restTemplate = new RestTemplate();
        this.oauthProviderCircuitBreaker = circuitBreakerFactory.create("oauthProviderCB");
    }

    public Map<String, Object> getUserInfo(String userInfoEndpoint, String accessToken) {
        return oauthProviderCircuitBreaker.run(() -> {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ParameterizedTypeReference<Map<String, Object>> responseType = 
                new ParameterizedTypeReference<Map<String, Object>>() {};
                
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                userInfoEndpoint,
                HttpMethod.GET,
                entity,
                responseType
            );
            
            return response.getBody();
        }, throwable -> {
            // Fallback logic when circuit is open
            return Map.of(
                "error", "Service Unavailable",
                "message", "OAuth provider is currently unavailable"
            );
        });
    }

    public Map<String, Object> exchangeCodeForToken(String tokenEndpoint, Map<String, String> params) {
        return oauthProviderCircuitBreaker.run(() -> {
            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(params);
            
            ParameterizedTypeReference<Map<String, Object>> responseType = 
                new ParameterizedTypeReference<Map<String, Object>>() {};
                
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                tokenEndpoint,
                HttpMethod.POST,
                requestEntity,
                responseType
            );
            
            return response.getBody();
        }, throwable -> {
            // Fallback logic when circuit is open
            return Map.of(
                "error", "Service Unavailable",
                "message", "OAuth token exchange service is currently unavailable"
            );
        });
    }
}
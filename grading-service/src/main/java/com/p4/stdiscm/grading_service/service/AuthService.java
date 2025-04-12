package com.p4.stdiscm.grading_service.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final TokenValidationService tokenValidationService;

    public AuthService(TokenValidationService tokenValidationService) {
        this.tokenValidationService = tokenValidationService;
    }

    @CircuitBreaker(name = "authService", fallbackMethod = "fallbackValidateToken")
    public boolean validateToken(String username, String token) {
        return tokenValidationService.isTokenValid(username, token);
    }

    // Fallback method when circuit is open
    @SuppressWarnings("unused")
    private boolean fallbackValidateToken(String username, String token, Throwable t) {
        // Log the error
        System.err.println("Circuit breaker activated for token validation: " + t.getMessage());
        
        // In a fallback scenario, we might want to be permissive or restrictive
        // Here we're choosing to be restrictive for security
        return false;
    }
}

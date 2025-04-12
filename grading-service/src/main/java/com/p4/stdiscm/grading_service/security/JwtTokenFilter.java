package com.p4.stdiscm.grading_service.security;

import com.p4.stdiscm.grading_service.service.AuthService;

import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenFilter extends OncePerRequestFilter {

    private final AuthService authService;

    public JwtTokenFilter(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // Get the JWT token from the request
        String token = extractTokenFromRequest(request);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        // Debug information
        if (token != null) {
            System.out.println("Processing token: " + token.substring(0, Math.min(10, token.length())) + "...");
            if (auth != null) {
                System.out.println("Authentication found: " + auth.getName() + ", authorities: " + auth.getAuthorities());
            } else {
                System.out.println("No authentication in context yet");
            }
        }
        
        // If we have authentication from JWT that's been processed by Spring Security
        if (auth instanceof JwtAuthenticationToken && token != null) {
            String username = auth.getName();
            
            // Check if token is valid in Redis (not revoked)
            if (!authService.validateToken(username, token)) {
                System.out.println("Token invalid or revoked for user: " + username);
                SecurityContextHolder.clearContext();
            } else {
                System.out.println("Token validated successfully for user: " + username);
            }
        }
        
        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
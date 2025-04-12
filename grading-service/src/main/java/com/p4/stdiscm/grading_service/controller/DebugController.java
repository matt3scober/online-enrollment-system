package com.p4.stdiscm.grading_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    @GetMapping("/token")
    public ResponseEntity<Map<String, Object>> debugToken(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        Map<String, Object> response = new HashMap<>();
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.put("error", "No Bearer token found");
            return ResponseEntity.ok(response);
        }
        
        String token = authHeader.substring(7);
        response.put("token", token);
        
        try {
            // Decode JWT parts
            String[] chunks = token.split("\\.");
            if (chunks.length == 3) {
                String header = new String(Base64.getDecoder().decode(chunks[0]));
                String payload = new String(Base64.getDecoder().decode(chunks[1]));
                
                response.put("header", header);
                response.put("payload", payload);
            }
        } catch (Exception e) {
            response.put("error", "Failed to decode token: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
}
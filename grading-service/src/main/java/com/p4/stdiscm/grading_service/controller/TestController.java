package com.p4.stdiscm.grading_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/public")
    public ResponseEntity<String> publicEndpoint() {
        return ResponseEntity.ok("This is a public endpoint");
    }

    @GetMapping("/auth")
    public ResponseEntity<Map<String, Object>> testAuth(Authentication auth) {
        Map<String, Object> response = new HashMap<>();
        
        if (auth != null) {
            response.put("authenticated", true);
            response.put("name", auth.getName());
            response.put("principal", auth.getPrincipal().toString());
            response.put("authorities", auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()));
        } else {
            response.put("authenticated", false);
        }
        
        return ResponseEntity.ok(response);
    }
}
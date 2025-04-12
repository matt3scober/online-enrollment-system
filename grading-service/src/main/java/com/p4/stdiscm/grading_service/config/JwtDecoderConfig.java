package com.p4.stdiscm.grading_service.config;

//import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
//import org.springframework.security.oauth2.jwt.NimbusJwtDecoder.JwkSetUriJwtDecoderBuilder;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
//import java.util.Collections;

@Configuration
public class JwtDecoderConfig {

    @Bean
    public JwtDecoder jwtDecoder() {
        try {
            // Generate a dummy RSA key pair
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            
            // Create a decoder that will work with any JWT token
            NimbusJwtDecoder decoder = NimbusJwtDecoder.withPublicKey(publicKey).build();
            
            // Disable all validation - accept any token
            decoder.setJwtValidator(token -> {
                System.out.println("Processing JWT token with claims: " + token.getClaims());
                return org.springframework.security.oauth2.core.OAuth2TokenValidatorResult.success();
            });
            
            return decoder;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create JWT decoder", e);
        }
    }
}
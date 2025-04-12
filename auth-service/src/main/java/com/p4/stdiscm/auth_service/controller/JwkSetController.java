package com.p4.stdiscm.auth_service.controller;

import com.p4.stdiscm.auth_service.config.JwtProperties;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;
import java.util.UUID;

@RestController
public class JwkSetController {

    private final JWKSet jwkSet;

    public JwkSetController(JwtProperties jwtProperties) throws Exception {
        // Generate RSA key pair for signing tokens
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // Convert to JWK format
        RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey(keyPair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .keyUse(com.nimbusds.jose.jwk.KeyUse.SIGNATURE)
                .build();

        jwkSet = new JWKSet(rsaKey);
    }

    @GetMapping("/.well-known/jwks.json")
    public Map<String, Object> getJwkSet() {
        return jwkSet.toJSONObject();
    }
}
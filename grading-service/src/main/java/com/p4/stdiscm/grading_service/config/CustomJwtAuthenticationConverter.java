package com.p4.stdiscm.grading_service.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        Collection<GrantedAuthority> authorities = extractAuthorities(jwt);
        
        // Debug information
        System.out.println("JWT Claims: " + jwt.getClaims());
        System.out.println("JWT Subject: " + jwt.getSubject());
        System.out.println("JWT Roles: " + jwt.getClaim("roles"));
        System.out.println("Extracted Authorities: " + authorities);
        
        return new JwtAuthenticationToken(jwt, authorities, jwt.getSubject());
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        if (jwt.getClaim("roles") instanceof String) {
            // Handle single role as string
            String role = jwt.getClaimAsString("roles");
            System.out.println("Extracted single role: " + role);
            return Collections.singletonList(new SimpleGrantedAuthority(role));
        } else if (jwt.getClaim("roles") instanceof Collection) {
            // Handle multiple roles as collection
            Collection<String> roles = jwt.getClaim("roles");
            System.out.println("Extracted multiple roles: " + roles);
            return roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
        System.out.println("No roles found in token");
        return Collections.emptyList();
    }
}
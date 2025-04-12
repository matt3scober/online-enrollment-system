package com.p4.stdiscm.grading_service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.p4.stdiscm.grading_service.security.JwtTokenFilter;
import com.p4.stdiscm.grading_service.service.AuthService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private AuthService authService;
    
    @Autowired
    private CustomJwtAuthenticationConverter customJwtAuthenticationConverter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/api/test/**").permitAll() // Allow test endpoint
                .requestMatchers("/api/grades/*/view").hasAnyAuthority("ROLE_USER", "ROLE_MODERATOR", "ROLE_ADMIN")
                .requestMatchers("/api/grades/**").hasAnyAuthority("ROLE_MODERATOR", "ROLE_ADMIN")
                .requestMatchers("/api/test/auth").permitAll() 
                .requestMatchers("/api/debug/**").permitAll() 
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .jwtAuthenticationConverter(customJwtAuthenticationConverter)
                )
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterAfter(new JwtTokenFilter(authService), UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
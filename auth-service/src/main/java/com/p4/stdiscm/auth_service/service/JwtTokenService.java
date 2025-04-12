package com.p4.stdiscm.auth_service.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.p4.stdiscm.auth_service.config.JwtProperties;

import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class JwtTokenService {

    private final JwtProperties jwtProperties;
    private final RedisTemplate<String, Object> redisTemplate;
    private final Key key;

    public JwtTokenService(JwtProperties jwtProperties, RedisTemplate<String, Object> redisTemplate) {
        this.jwtProperties = jwtProperties;
        this.redisTemplate = redisTemplate;
        
        // Use Keys.secretKeyFor to generate a secure key for HS512
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        
        // Alternative approach if you want to keep using your configured secret,
        // but make sure it's long enough (64+ characters for HS512)
        // this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public String generateToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getExpirationMs());
        
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String token = Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim("roles", roles)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .setIssuer(jwtProperties.getIssuer())
                .setAudience(jwtProperties.getAudience())
                .signWith(key) // No need to specify algorithm, it's inferred from the key
                .compact();
        
        // Store token in Redis with expiration
        String redisKey = "token:" + userPrincipal.getUsername();
        redisTemplate.opsForValue().set(redisKey, token, jwtProperties.getExpirationMs(), TimeUnit.MILLISECONDS);
        
        return token;
    }
    
    // Rest of your methods remain the same
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        
        return claims.getSubject();
    }
    
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            
            // Also check if token exists in Redis
            String username = getUsernameFromToken(token);
            String redisKey = "token:" + username;
            Object storedToken = redisTemplate.opsForValue().get(redisKey);
            
            return storedToken != null && storedToken.toString().equals(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    
    public void invalidateToken(String username) {
        String redisKey = "token:" + username;
        redisTemplate.delete(redisKey);
    }
}
package com.p4.stdiscm.grading_service.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class TokenValidationService {

    private final RedisTemplate<String, String> redisTemplate;

    public TokenValidationService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isTokenValid(String username, String token) {
        String key = "token:" + username;
        String storedToken = redisTemplate.opsForValue().get(key);
        return token != null && token.equals(storedToken);
    }
}

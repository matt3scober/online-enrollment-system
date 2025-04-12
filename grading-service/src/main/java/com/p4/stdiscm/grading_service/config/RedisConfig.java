package com.p4.stdiscm.grading_service.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {

        @Bean
        public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
                RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(30))
                        .disableCachingNullValues()
                        .serializeValuesWith(
                                RedisSerializationContext.SerializationPair.fromSerializer(
                                        new GenericJackson2JsonRedisSerializer()));

                return RedisCacheManager.builder(connectionFactory)
                        .cacheDefaults(config)
                        .withCacheConfiguration("studentGrades", 
                                RedisCacheConfiguration.defaultCacheConfig()
                                        .entryTtl(Duration.ofMinutes(10)))
                        .withCacheConfiguration("courseGrades", 
                                RedisCacheConfiguration.defaultCacheConfig()
                                        .entryTtl(Duration.ofMinutes(20)))
                        .build();
        }

        @Bean
        public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
                RedisTemplate<String, String> template = new RedisTemplate<>();
                template.setConnectionFactory(connectionFactory);
                template.setKeySerializer(new StringRedisSerializer());
                template.setValueSerializer(new StringRedisSerializer());
                return template;
        }
}

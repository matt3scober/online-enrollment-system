package com.p4.stdiscm.enrollment_service.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        // Make sure Redis is running on localhost at port 6379 or adjust accordingly
        config.useSingleServer().setAddress("redis://localhost:6379");
        return Redisson.create(config);
    }
}

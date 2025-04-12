package com.p4.stdiscm.grading_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableCaching
@EnableJpaRepositories
@EnableMethodSecurity
public class GradingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GradingServiceApplication.class, args);
    }
}

package com.p4.stdiscm.auth_service.config;

import com.p4.stdiscm.auth_service.model.Role;
import com.p4.stdiscm.auth_service.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        // Initialize roles if they don't exist
        if (roleRepository.findByName("ROLE_USER").isEmpty()) {
            roleRepository.save(new Role("ROLE_USER"));
        }

        if (roleRepository.findByName("ROLE_MODERATOR").isEmpty()) {
            roleRepository.save(new Role("ROLE_MODERATOR"));
        }

        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            roleRepository.save(new Role("ROLE_ADMIN"));
        }
    }
}
package com.odp.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.odp.main.Models.Admin;
import com.odp.main.Repositorys.AdminRepository;

@Component
public class DataInitializer {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initDatabase() {
        return args -> {
            // Check if an admin user already exists
            Admin existingAdmin = adminRepository.findByUsername("admin");
            if (existingAdmin == null) {
                // If no admin exists, create one
                Admin admin = new Admin();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin"));
                // Save the new admin to the repository
                adminRepository.save(admin);
            }
        };
    }
}

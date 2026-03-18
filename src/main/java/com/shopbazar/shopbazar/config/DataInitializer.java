package com.shopbazar.shopbazar.config;

import com.shopbazar.shopbazar.entity.Role;
import com.shopbazar.shopbazar.entity.User;
import com.shopbazar.shopbazar.repository.RoleRepository;
import com.shopbazar.shopbazar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Initialize Roles
        for (Role.RoleName roleName : Role.RoleName.values()) {
            if (roleRepository.findByRoleName(roleName).isEmpty()) {
                Role role = Role.builder()
                        .roleName(roleName)
                        .description("Role for " + roleName.name())
                        .build();
                roleRepository.save(role);
            }
        }

        // Initialize Admin User
        String adminEmail = "admin@shopbazar.com";
        if (!userRepository.existsByEmail(adminEmail)) {
            Role adminRole = roleRepository.findByRoleName(Role.RoleName.ADMIN)
                    .orElseThrow(() -> new RuntimeException("ADMIN role not found"));

            User admin = User.builder()
                    .firstName("System")
                    .lastName("Admin")
                    .email(adminEmail)
                    .password(passwordEncoder.encode("admin123"))
                    .role(adminRole)
                    .status(User.Status.ACTIVE)
                    .emailVerified(true)
                    .build();
            userRepository.save(admin);
        }
    }
}

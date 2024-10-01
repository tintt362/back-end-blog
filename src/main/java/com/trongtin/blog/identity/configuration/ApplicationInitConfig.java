package com.trongtin.blog.identity.configuration;

import com.trongtin.blog.identity.constant.PredefinedRole;
import com.trongtin.blog.identity.entity.Role;
import com.trongtin.blog.identity.entity.User;
import com.trongtin.blog.identity.repository.RoleRepository;
import com.trongtin.blog.identity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class ApplicationInitConfig {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Bean
    ApplicationRunner  applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {

                Role adminRole = roleRepository.save(Role.builder()
                        .name(PredefinedRole.ADMIN_ROLE)
                        .description("Admin role")
                        .build());
                Set<Role> roles= new HashSet<>();
                roles.add(adminRole);
                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles(roles)
                        .build();
                userRepository.save(user);

            }
        };
    }
}

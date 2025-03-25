package com.example.orderproduct.config.custom;

import com.example.orderproduct.entity.UserEntity;
import com.example.orderproduct.entity.UserRoleEnity;
import com.example.orderproduct.repository.UserRepository;
import com.example.orderproduct.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Optional<UserEntity> user = userRepository.findByUserName("superadmin");
        if (user.isEmpty()) {
            UserEntity admin = new UserEntity();
            admin.setUsername("superadmin");
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setStatus(1L);
            userRepository.save(admin);
            UserRoleEnity userRole = new UserRoleEnity();
            userRole.setUserId(admin.getId());
            userRole.setRoleId(3L);
            userRole.setStatus(1L);
            userRoleRepository.save(userRole);
        }
        else {
            UserEntity admin = user.get();
            UserRoleEnity userRole = userRoleRepository.findUserRoleByUserId(admin.getId()).orElse(null);
            if (userRole != null) {
                userRole.setRoleId(3L);
                userRole.setStatus(1L);
                userRoleRepository.save(userRole);
            }
            else {
                userRole = new UserRoleEnity();
                userRole.setUserId(admin.getId());
                userRole.setRoleId(3L);
                userRole.setStatus(1L);
                userRoleRepository.save(userRole);
            }
        }
    }
}

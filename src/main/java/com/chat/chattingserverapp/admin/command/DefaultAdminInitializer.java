package com.chat.chattingserverapp.admin.command;

import com.chat.chattingserverapp.admin.domain.Admin;
import com.chat.chattingserverapp.admin.infrastructure.AdminRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultAdminInitializer implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        String defaultEmail = "admin@example.com";
        
        if (adminRepository.findByEmail(defaultEmail).isPresent()) {
            log.info("Default admin account already exists");
            return;
        }

        AdminCreateCommand command = new AdminCreateCommand(
            UUID.randomUUID(),
            defaultEmail,
            "admin123!@#",
            "관리자"
        );

        String hashedPassword = passwordEncoder.encode(command.password());
        Admin admin = command.toAdmin(hashedPassword);
        
        adminRepository.save(admin);
        log.info("Default admin account created successfully");
    }
} 
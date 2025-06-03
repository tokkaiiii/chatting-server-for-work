package com.chat.chattingserverapp.admin.service;

import com.chat.chattingserverapp.admin.domain.Admin;
import com.chat.chattingserverapp.admin.infrastructure.AdminRepository;
import com.chat.chattingserverapp.admin.response.AdminResponse;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceAdapter implements AdminService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<AdminResponse> findByEmail(String email, String password) {
        return adminRepository.findByEmail(email)
            .filter(admin -> admin.decodePassword(passwordEncoder, password))
            .map(AdminResponse::from);
    }

    @Override
    public Optional<AdminResponse> findById(UUID id) {
        return adminRepository.findById(id)
            .map(AdminResponse::from);
    }

    @Override
    public boolean existsById(UUID id) {
        return adminRepository.existsById(id);
    }

    @Override
    public Admin getDefaultAdmin() {
        return adminRepository.findByEmail("admin@example.com")
            .orElseThrow(() -> new IllegalStateException("Default admin not found"));
    }

    @Override
    public Optional<Admin> findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }
}

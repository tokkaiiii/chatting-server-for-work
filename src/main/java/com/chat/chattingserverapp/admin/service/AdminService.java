package com.chat.chattingserverapp.admin.service;

import com.chat.chattingserverapp.admin.domain.Admin;
import com.chat.chattingserverapp.admin.response.AdminResponse;
import java.util.Optional;
import java.util.UUID;

public interface AdminService {
    Optional<AdminResponse> findByEmail(String email, String password);
    Optional<AdminResponse> findById(UUID id);
    boolean existsById(UUID id);
    Admin getDefaultAdmin();
    Optional<Admin> findByEmail(String email);
}

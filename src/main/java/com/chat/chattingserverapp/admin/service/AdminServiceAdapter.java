package com.chat.chattingserverapp.admin.service;

import com.chat.chattingserverapp.admin.infrastructure.AdminRepository;
import com.chat.chattingserverapp.admin.query.IssueAdminToken;
import com.chat.chattingserverapp.admin.response.AdminResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceAdapter implements AdminService{
  private final AdminRepository adminRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Optional<AdminResponse> findByEmail(IssueAdminToken query) {
    return adminRepository.findByEmail(query.email())
        .filter(admin -> admin.decodePassword(passwordEncoder, query.password()))
        .map(AdminResponse::from);
  }
}

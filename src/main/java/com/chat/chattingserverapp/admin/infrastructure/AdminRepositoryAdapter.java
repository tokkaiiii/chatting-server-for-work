package com.chat.chattingserverapp.admin.infrastructure;

import com.chat.chattingserverapp.admin.domain.Admin;
import com.chat.chattingserverapp.common.infrastructure.SpringDataAdminRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AdminRepositoryAdapter implements AdminRepository {

  private final SpringDataAdminRepository jpaAdminRepository;

  @Override
  public Optional<Admin> findByEmail(String email) {
    return jpaAdminRepository.findByEmail(email);
  }
}

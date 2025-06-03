package com.chat.chattingserverapp.admin.infrastructure;

import com.chat.chattingserverapp.admin.domain.Admin;
import com.chat.chattingserverapp.common.infrastructure.SpringDataAdminRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AdminRepositoryAdapter implements AdminRepository {

  private final SpringDataAdminRepository jpaAdminRepository;

  @Override
  public Optional<Admin> findById(UUID id) {
    return jpaAdminRepository.findById(id);
  }

  @Override
  public boolean existsById(UUID id) {
    return findById(id).isPresent();
  }

  @Override
  public Optional<Admin> findByEmail(String email) {
    return jpaAdminRepository.findByEmail(email);
  }

  @Override
  public Admin save(Admin admin) {
    return jpaAdminRepository.save(admin);
  }
}

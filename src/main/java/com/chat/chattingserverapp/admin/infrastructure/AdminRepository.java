package com.chat.chattingserverapp.admin.infrastructure;

import com.chat.chattingserverapp.admin.domain.Admin;
import java.util.Optional;
import java.util.UUID;

public interface AdminRepository {

  Optional<Admin> findByEmail(String email);

  Optional<Admin> findById(UUID id);

  boolean existsById(UUID id);

  Admin save(Admin admin);
}

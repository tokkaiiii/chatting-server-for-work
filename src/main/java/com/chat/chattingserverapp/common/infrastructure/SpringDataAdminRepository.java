package com.chat.chattingserverapp.common.infrastructure;

import com.chat.chattingserverapp.admin.domain.Admin;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataAdminRepository extends JpaRepository<Admin,Long> {

  Optional<Admin> findByEmail(String email);

  Optional<Admin> findById(UUID id);
}

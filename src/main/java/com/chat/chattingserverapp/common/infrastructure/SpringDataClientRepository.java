package com.chat.chattingserverapp.common.infrastructure;

import com.chat.chattingserverapp.client.domain.Client;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataClientRepository extends JpaRepository<Client, Long> {

  Optional<Client> findByUsername(String username);

  Optional<Client> findById(UUID id);

  Optional<Client> findByEmail(String email);
}

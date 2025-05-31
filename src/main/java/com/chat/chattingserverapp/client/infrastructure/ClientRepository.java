package com.chat.chattingserverapp.client.infrastructure;

import com.chat.chattingserverapp.client.domain.Client;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientRepository {

  Optional<Client> save(Client client);

  Optional<Client> findByUsername(String username);

  List<Client> findAll();

  Optional<Client> findById(UUID id);
}

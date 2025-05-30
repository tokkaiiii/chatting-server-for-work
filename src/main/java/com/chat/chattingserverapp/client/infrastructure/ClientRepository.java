package com.chat.chattingserverapp.client.infrastructure;

import com.chat.chattingserverapp.client.domain.Client;
import java.util.List;
import java.util.Optional;

public interface ClientRepository {

  Optional<Client> save(Client client);

  Optional<Client> findByUsername(String username);

  List<Client> findAll();

}
